import {Component, Injectable, OnInit} from '@angular/core';
import {Node} from "../model/Node";
import {Link} from "../model/Link";
import {ApiService} from "../../shared/api.service";
import {NodeType} from "../model/node-type.enum";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Simulation} from "../model/Simulation";
import {AuthService} from "../../auth/shared/auth.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-workspace',
  templateUrl: './workspace.component.html',
  styleUrls: ['./workspace.component.css']
})
@Injectable({
  providedIn: 'root'
})
export class WorkspaceComponent implements OnInit {

  //Flagi sterujące
  leftSideBarOpened = true;
  drawingMode = false;
  loading = false;
  pingingBack = false;

  simulation = new Simulation();

  consoleContent = "";

  lastCreatedInterface = null;
  lastCreatedNode = null;

  //Stałe do rysowania
  HALF_IMAGE_LENGTH = 20;
  ROUTER_RADIUS = this.HALF_IMAGE_LENGTH * 2; // promień ikony routera
  CLIENT_RADIUS = this.ROUTER_RADIUS * 1.3; // promień ikony klienta

  //Kolumny tabel z parametrami
  nodeParametersColumns: string[] = ['name', 'type'];
  routingTableColumns: string[] = ['network', 'next-hop'];

  newRowForm: FormGroup;

  clientInterfacesCounter = 1;
  simulationStarted: boolean;

  pingMode: boolean;
  sourceNode: Node;

  constructor(private apiService : ApiService, private authService: AuthService, private toastrService : ToastrService) {}

  ngOnInit() {
    this.newRowForm = new FormGroup({
      network: new FormControl(''),
      next_hop: new FormControl(''),
    });

    this.loading = true;

    this.apiService.currentId.subscribe(id => this.simulation.id = id);
    if (this.simulation.id == '') {
      this.apiService.getUserLastSimulation(this.authService.getUserName()).subscribe(id => {
        this.simulation.id = id;
        if (this.simulation.id != null) {
          this.apiService.getSimulation(this.simulation.id).subscribe(
            response => {
              this.simulation = response;
              this.drawLinksAndInterfaces();
            },
            err => {
              alert("An error occured when getting simulation from the server!")
            }
          );
        }
      });
    } else {
      this.apiService.getSimulation(this.simulation.id).subscribe(
        response => {
          this.simulation = response;
          this.drawLinksAndInterfaces();
        },
        err => {
          alert("An error occured when getting simulation from the server!")
        }
      );
    }
  }

  //Dodaj nowy węzeł do pola roboczego
  addNode(nodeType : string) {
    let node = new Node();
    node.type = nodeType == NodeType.ROUTER ? NodeType.ROUTER : NodeType.CLIENT;
    this.simulation.nodes.push(node);
  }

  addInterface(node : Node) {
    let newInterface;
    if (node.type == NodeType.CLIENT) {
      if (node.interfaces.length == 0) {
        newInterface = "192.168.1." + this.getClientInterfaceNumber();
        node.interfaces.push(newInterface);
      } else {
        alert("Client node can only have 1 interface!");
        this.drawingMode = false;
      }
    } else {
      newInterface = "10.1." + (node.interfaces.length + 1) + "." + this.checkIfLinkExistsAndReturnNumber();
      node.interfaces.push(newInterface);
    }

    if (this.lastCreatedInterface == null || this.lastCreatedNode == null) {
      this.lastCreatedInterface = newInterface;
      this.lastCreatedNode = node;
    } else {
      this.addLink(node, this.lastCreatedNode, newInterface, this.lastCreatedInterface);
      this.lastCreatedInterface = null;
    }
  }

  addLink(nodeA : Node, nodeB : Node, interfaceA : string, interfaceB : string) {
    let link = new Link(nodeA, nodeB, interfaceA, interfaceB);
    this.simulation.links.push(link);
    this.drawLinksAndInterfaces();
  }

  saveLink(link) {
    this.apiService.postLink(link).subscribe(
      response => {
        link.id = response.id;
        link.nodeA = response.nodeA;
        link.nodeB = response.nodeB;

        link.xA = response.xA;
        link.yA = response.yA;
        link.xB = response.xB;
        link.yB = response.yB;

        link.interfaceA = response.interfaceA;
        link.interfaceB = response.interfaceB;

        this.simulation.links.push(link);
      },
      error => {
        alert("An error occured - Cannot add new link!");
      }
    );
  }

  updateParameters(event, node) {
    let element = event.source.getRootElement();
    let boundingClientRect = element.getBoundingClientRect();
    let parentPosition = this.getPosition(element);

    let previousCoordinates = [node.actualX, node.actualY]

    node.previousX += node.actualX == undefined ? 0 : node.actualX;
    node.previousY += node.actualY == undefined ? 0 : node.actualY;

    node.actualX = boundingClientRect.x - parentPosition.left;
    node.actualY = boundingClientRect.y - parentPosition.top;

    this.updateLinkPosition(previousCoordinates, node);
  }

  updateLinkPosition(previousCoordinates: any[], node : Node) {

    for (let link of this.simulation.links) {
      if (link.nodeA.actualX == previousCoordinates[0] && link.nodeA.actualY == previousCoordinates[1]) {
        link.nodeA = node;
      }
      if (link.nodeB.actualX == previousCoordinates[0] && link.nodeB.actualY == previousCoordinates[1]) {
        link.nodeB = node;
      }
    }
    this.clearLinks();
    this.drawLinksAndInterfaces();
  }

  //Czyści całe pole robocze
  resetWorkspace() {
    this.simulation = new Simulation();
    this.clearLinks();
  }

  saveSimulation() {
    this.apiService.saveSimulation(this.authService.getUserName(), this.simulation).subscribe(
      response => {
        this.toastrService.success('Simulation saved successfully!');
      },
      error => {
        alert("An error occured - Cannot save simulation!");
      }
    );
  }

  removeSelectedNode() {
    for (let node of this.simulation.nodes) {
      if (node.selected) {
        this.simulation.links = this.simulation.links.filter(link => (link.nodeA.actualX != node.actualX && link.nodeB.actualX != node.actualX));
      }
    }
    this.simulation.nodes = this.simulation.nodes.filter(node => !node.selected);
    this.clearLinks();
    this.drawLinksAndInterfaces();
  }

  private drawLinksAndInterfaces() {
    for (let link of this.simulation.links) {
      var c =  <HTMLCanvasElement> document.getElementById("myCanvas");
      var ctx = c.getContext("2d");

      ctx.beginPath();
      ctx.lineWidth=2;
      ctx.strokeStyle="gray";

      [link.xA, link.yA, link.xB, link.yB] = this.calculateLinkDrawpoint(
        link.nodeA.actualX + link.nodeA.previousX + this.HALF_IMAGE_LENGTH,
        link.nodeA.actualY + link.nodeA.previousY + this.HALF_IMAGE_LENGTH,
        link.nodeB.actualX + link.nodeB.previousX + this.HALF_IMAGE_LENGTH,
        link.nodeB.actualY + link.nodeB.previousY + this.HALF_IMAGE_LENGTH,
        link.nodeA.type, link.nodeB.type);
      ctx.moveTo(link.xA, link.yA);
      ctx.lineTo(link.xB, link.yB);
      ctx.stroke();
    }
  }

  clearLinks() {
    var c =  <HTMLCanvasElement> document.getElementById("myCanvas");
    var ctx = c.getContext("2d");
    ctx.clearRect(0, 0, c.width, c.height);
  }

  select(id: string) {
    for (let node of this.simulation.nodes) {
      node.selected = node.id == id;
    }
  }

  getPosition(el) {
    let x = 0;
    let y = 0;
    while(el && !isNaN(el.offsetLeft) && !isNaN(el.offsetTop)) {
      x += el.offsetLeft - el.scrollLeft;
      y += el.offsetTop - el.scrollTop;
      el = el.offsetParent;
    }
    return { top: y, left: x };
  }

  //Decyzja którą ikonę wyświetlać w zależności od typu węzła
  getImageSource(nodeType: NodeType) : string {
    switch (nodeType) {
      case NodeType.ROUTER:
        return 'assets/images/router.png';
      case NodeType.CLIENT:
        return 'assets/images/client.png';
      default:
        return '';
    }
  }

  //Wykonuje obliczenia niezbędne do prawidłowego
  //rysowania połączeń między węzłami
  calculateLinkDrawpoint(x1 : number, y1 : number, x2 : number, y2 : number, type1 : NodeType, type2 : NodeType) {
    let xDiff = Math.abs(x1 - x2);
    let yDiff = Math.abs(y1 - y2);
    let multiplier = xDiff / yDiff;

    if (multiplier > 1) {
      if (x1 > x2 && y1 > y2) {
        return [x1 - this.getRadius(type1), y1 - (this.getRadius(type1) * (1/multiplier)), x2 + this.getRadius(type2), y2 + (this.getRadius(type2) * (1/multiplier))];
      } else if (x1 > x2 && y1 < y2) {
        return [x1 - this.getRadius(type1), y1 + (this.getRadius(type1) * (1/multiplier)), x2 + this.getRadius(type2), y2 - (this.getRadius(type2) * (1/multiplier))];
      } else if (x1 < x2 && y1 > y2) {
        return [x1 + this.getRadius(type1), y1 - (this.getRadius(type1) * (1/multiplier)), x2 - this.getRadius(type2), y2 + (this.getRadius(type2) * (1/multiplier))];
      } else if (x1 < x2 && y1 < y2) {
        return [x1 + this.getRadius(type1), y1 + (this.getRadius(type1) * (1/multiplier)), x2 - this.getRadius(type2), y2 - (this.getRadius(type2) * (1/multiplier))];
      } else {
        return [x1, y1, x2, y2];
      }
    } else {
      if (x1 > x2 && y1 > y2) {
        return [x1 - (this.getRadius(type1) * multiplier), y1 - this.getRadius(type1), x2 + (this.getRadius(type2) * multiplier), y2 + this.getRadius(type2)];
      } else if (x1 > x2 && y1 < y2) {
        return [x1 - (this.getRadius(type1) * multiplier), y1 + this.getRadius(type1), x2 + (this.getRadius(type2) * multiplier), y2 - this.getRadius(type2)];
      } else if (x1 < x2 && y1 > y2) {
        return [x1 + (this.getRadius(type1) * multiplier), y1 - this.getRadius(type1), x2 - (this.getRadius(type2) * multiplier), y2 + this.getRadius(type2)];
      } else if (x1 < x2 && y1 < y2) {
        return [x1 + (this.getRadius(type1) * multiplier), y1 + this.getRadius(type1), x2 - (this.getRadius(type2) * multiplier), y2 - this.getRadius(type2)];
      } else {
        return [x1, y1, x2, y2];
      }
    }
  }

  getRadius(nodeType : NodeType) {
    switch (nodeType) {
      case NodeType.ROUTER:
        return this.ROUTER_RADIUS;
      case NodeType.CLIENT:
      default:
        return this.CLIENT_RADIUS;
    }
  }

  unselect() {
    for (let node of this.simulation.nodes) {
      if (node.selected) {
        node.selected = false;
      }
    }
  }

  getRoutingTableAsPairTable(node: Node) {
    let routingTableRows = [];

    for (let pair of Object.entries(node.routingTable)) {
      routingTableRows.push({network: pair[0], next_hop: pair[1]});
    }

    return routingTableRows;
  }

  addRow(node : Node) {
    let map = new Map(Object.entries(node.routingTable));
    map.set(this.newRowForm.get('network').value, this.newRowForm.get('next_hop').value);

    const convMap = {};
    map.forEach((val: string, key: string) => {
      convMap[key] = val;
    });

    this.apiService.postRoutingTableRow(node.id, convMap).subscribe(
      response => {
        node = response;
      },
      error => {
        alert("An error occured - Cannot update node parameters!");
      }
    );
  }

  checkIfLinkExistsAndReturnNumber() {
    if (this.lastCreatedInterface == null) {
      return 1;
    } else {
      return 2;
    }
  }

  getClientInterfaceNumber() {
    for (let node of this.simulation.nodes) {
      if (node.type == NodeType.CLIENT && node.interfaces.length > 0) {
        ++this.clientInterfacesCounter;
      }
    }
    return this.clientInterfacesCounter;
  }

  checkIfNodeSelected() {
    for (let node of this.simulation.nodes) {
      if (node.selected) {
        return false;
      }
    }
    return true;
  }

  ping(destinationNode : Node) {
    if (this.simulationStarted) {
      if (destinationNode.type != NodeType.CLIENT) {
        alert("Only client nodes are able to send messages!");
      } else {
        if (this.sourceNode == null) {
          this.sourceNode = destinationNode;
        } else {
          let date = new Date();
          this.consoleContent += "[" + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds() + "]" + " Checking " + this.sourceNode.name + " routing table...\n";
          this.checkPath(this.sourceNode, destinationNode.interfaces);
        }
      }
    } else {
      alert("Simulation must be started!");
    }
  }

  checkPath(sourceNode : Node, interfaces : string[]) {
    if (this.checkIfDestinationNodeReached(sourceNode, interfaces) && !this.pingingBack) {
      this.pingingBack = true;
      this.checkPath(sourceNode, this.sourceNode.interfaces);
    }
    for (let i of interfaces) {
      let map = new Map(Object.entries(sourceNode.routingTable));
      if (map.has(i)) {
        for (let node of this.simulation.nodes) {
          let newInterface = map.get(i);
          if (node.interfaces.includes(newInterface)) {
            let date = new Date();
            this.consoleContent += "[" + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds() + "]" + " Message forwarded from " + sourceNode.name + " to " + node.name + "\n";
            this.checkPath(node, interfaces);
          }
        }
      }
    }
  }

  checkIfDestinationNodeReached(sourceNode : Node, destinationInterfaces : string[]) {
    if (sourceNode.interfaces.filter(value => destinationInterfaces.includes(value)).length > 0) {
      let date = new Date();
      this.consoleContent +=  "[" + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds() + "]" + " Destination reached.";
      this.consoleContent += this.pingingBack ? "\n" : " Checking " + sourceNode.name + " routing table...\n";
      return true;
    }
    return false;
  }
}

export interface RoutingTableRow {
  network: string;
  next_hop: string;
}
