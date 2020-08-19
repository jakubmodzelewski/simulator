import {Component, Injectable, OnInit} from '@angular/core';
import {Node} from "../model/Node";
import {Link} from "../model/Link";
import {ApiService} from "../../shared/api.service";

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

  constructor(private apiService : ApiService) {}

  ngOnInit() {
    this.loading = true;
    this.getAllNodes();
    this.getAllLinks();
  }

  //Dodaj nowy węzeł do pola roboczego
  addNode() {
    let node = new Node();
    this.apiService.postNode(node).subscribe(
      response => {
        node.id = response.id;
        node.actualX = response.actualX;
        node.actualY = response.actualY;
        node.previousX = response.previousX;
        node.previousY = response.previousY;
        node.name = response.name;
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


  openNodeMenu(node) {
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
      ctx.lineWidth=1;
      ctx.strokeStyle="gray";
      ctx.moveTo(link.interfaceA.actualX,link.interfaceA.actualY);
      ctx.lineTo(link.interfaceB.actualX,link.interfaceB.actualY);
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
}
