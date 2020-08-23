import {Component, Injectable, OnInit} from '@angular/core';
import {Node} from "../model/Node";
import {Link} from "../model/Link";
import {ApiService} from "../../shared/api.service";
import {NodeType} from "../model/node-type.enum";

@Component({
  selector: 'app-workspace',
  templateUrl: './workspace.component.html',
  styleUrls: ['./workspace.component.css']
})
@Injectable({
  providedIn: 'root'
})
export class WorkspaceComponent implements OnInit {

  leftSideBarOpened = true;
  rightSideBarOpened = false;
  drawingMode = false;
  loading = false;

  nodes : Node[] = [];
  links : Link[] = [];

  lastCreatedInterface = null;

  HALF_IMAGE_LENGTH = 17.5;
  ROUTER_RADIUS = this.HALF_IMAGE_LENGTH * 1.41; // promień ikony routera
  CLIENT_RADIUS = this.ROUTER_RADIUS * 2; // promień ikony klienta

  nodeParametersColumns: string[] = ['name', 'type', 'loopback'];
  routingTableColumns: string[] = ['network', 'next-hop'];

  constructor(private apiService : ApiService) {}

  ngOnInit() {
    this.loading = true;
    this.getAllNodes();
    this.getAllLinks();
  }

  //Dodaj nowy węzeł do pola roboczego
  addNode(nodeType : string) {
    let node = new Node();
    node.type = nodeType == NodeType.ROUTER ? NodeType.ROUTER : NodeType.CLIENT;
    this.apiService.postNode(node).subscribe(
      response => {
        node.id = response.id;
        node.name = response.name;
        node.loopback = response.loopback;
        node.actualX = response.actualX;
        node.actualY = response.actualY;
        node.previousX = response.previousX;
        node.previousY = response.previousY;
        this.nodes.push(node);
      },
      error => {
        alert("An error occured - Cannot add new node!");
      }
    );
  }

  addInterface(node : Node) {
    node.addInterface();


    this.apiService.postNode(node).subscribe(
      response => {
      },
      error => {
        alert("An error occured - Cannot update node parameters!");
      }
    );

    if (this.lastCreatedInterface == null) {
      this.lastCreatedInterface = node;
    } else {
      this.addLink(node);
      this.lastCreatedInterface = null;
    }
  }

  addLink(node : Node) {
    let link = new Link(this.lastCreatedInterface, node);

    this.apiService.postLink(link).subscribe(
      response => {
        link.id = response.id;
        link.interfaceA = response.interfaceA;
        link.interfaceB = response.interfaceB;

        this.links.push(link);

      },
      error => {
        alert("An error occured - Cannot add new link!");
      }
    );
  }

  //Pobierz z serwera wszystkie węzły
  getAllNodes() {
    this.apiService.getAllNodes().subscribe(
      response => {
        this.nodes = response;
      },
      err => {
        alert("An error occured when getting nodes from the server!")
      }
    );
  }

  private getAllLinks() {
    this.apiService.getAllLinks().subscribe(
      response => {
        this.links = response;
        this.drawLinks();
      },
      err => {
        alert("An error occured when getting links from the server!")
      }
    );
  }

  //Po każdym przeciągnięciu node'a uaktualniamy
  //jego pozycję i wysyłamy do bazy
  updateParameters(event, node) {
    let element = event.source.getRootElement();
    let boundingClientRect = element.getBoundingClientRect();
    let parentPosition = this.getPosition(element);

    node.previousX += node.actualX == undefined ? 0 : node.actualX;
    node.previousY += node.actualY == undefined ? 0 : node.actualY;

    node.actualX = boundingClientRect.x - parentPosition.left;
    node.actualY = boundingClientRect.y - parentPosition.top;

    this.apiService.postNode(node).subscribe(
      response => {
      },
      error => {
        alert("An error occured - Cannot update node parameters!");
      }
    );
  }

  //Czyści całe pole robocze
  resetWorkspace() {
    this.apiService.deleteAllNodes().subscribe(
      response => {
        this.nodes = response;
      },
      error => {
        alert("An error occured during workspace reset")
      }
    );
  }

  saveParameters() {}

  loadSimulation() {}


  openNodeMenu() {
    this.rightSideBarOpened = true;
  }

  removeSelectedNode() {
    for(let node of this.nodes) {
      if (node.selected) {
        this.apiService.deleteNode(node.id).subscribe(
          response => {
            this.nodes = response;
          },
          error => {
            alert("An error occured during removing node")
          }
        );
      }
    }
  }

  private drawLinks() {
    for (let link of this.links) {
      var c =  <HTMLCanvasElement> document.getElementById("myCanvas");
      var ctx = c.getContext("2d");

      ctx.beginPath();
      ctx.lineWidth=2;
      ctx.strokeStyle="gray";

      let x1, y1, x2, y2;
      [x1, y1, x2, y2] = this.calculateLinkDrawpoint(
        link.interfaceA.actualX + link.interfaceA.previousX + this.HALF_IMAGE_LENGTH,
        link.interfaceA.actualY + link.interfaceA.previousY + this.HALF_IMAGE_LENGTH,
        link.interfaceB.actualX + link.interfaceB.previousX + this.HALF_IMAGE_LENGTH,
        link.interfaceB.actualY + link.interfaceB.previousY + this.HALF_IMAGE_LENGTH,
        link.interfaceA.type, link.interfaceB.type);
      ctx.moveTo(x1, y1);
      ctx.lineTo(x2, y2);
      ctx.stroke();
    }
  }

  select(id: string) {
    for (let node of this.nodes) {
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
    for (let node of this.nodes) {
      if (node.selected) {
        node.selected = false;
      }
    }
  }
}
