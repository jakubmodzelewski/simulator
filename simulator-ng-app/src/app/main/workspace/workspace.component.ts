import {Component, Injectable, Input, OnInit} from '@angular/core';
import {Router} from "../model/Router";
import {Link} from "../model/Link";
import {Client} from "../model/Client";
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

  @Input()
  routers : Router[] = [];
  links : Link[] = [];
  clients: Client[] = [];

  constructor(private apiService : ApiService) {}

  ngOnInit() {
    this.getAllRouters();
    this.getAllClients();
  }

  //Dodaj nowy router do pola roboczego
  addRouter() {
    let router = new Router();
    this.apiService.postRouter(router).subscribe(
      response => {
        router.id = response.id;
        router.x = response.x;
        router.y = response.y;
        this.routers.push(router);
      },
      error => {
        alert("An error occured - Cannot add new router!");
      }
    );
  }

  addClient() {
    let client = new Client();
    this.apiService.postClient(client).subscribe(
      response => {
        client.id = response.id;
        client.x = response.x;
        client.y = response.y;
        this.clients.push(client);
      },
      error => {
        alert("An error occured - Cannot add new client!");
      }
    );
  }

  //Pobierz z serwera wszystkie routery'y
  getAllRouters() {
    this.apiService.getAllRouters().subscribe(
      response => {
        this.routers = response;
      },
      err => {
        alert("An error occured when getting routers from the server!")
      }
    );
  }

  getAllClients() {
    this.apiService.getAllClients().subscribe(
      response => {
        this.clients = response;
      },
      err => {
        alert("An error occured when getting clients from the server!")
      }
    );
  }

  //Po każdym kliknięciu na node uaktualniamy jego pozycję
  //i wysyłamy do repozytorium
  updateParameters(event, node) {
    let element = event.source.getRootElement();
    let boundingClientRect = element.getBoundingClientRect();
    let parentPosition = this.getPosition(element);

    node.x = boundingClientRect.x - parentPosition.left;
    node.y = boundingClientRect.y - parentPosition.top;

    console.log(node.x + " " + node.y)

    // this.apiService.postNode(node).subscribe(
    //   response => {
    //   },
    //   error => {
    //     alert("An error occured - Cannot update node parameters!");
    //   }
    // );
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

  //Czyści całe pole robocze
  resetWorkspace() {
    this.apiService.resetWorkspace();
    this.routers = [];
    this.clients = [];
  }

  saveParameters() {}

  loadSimulation() {}


  openNodeMenu(node) {
    this.rightSideBarOpened = true;
  }

  addInterface() {
    if (this.drawingMode) {

    }
  }

}
