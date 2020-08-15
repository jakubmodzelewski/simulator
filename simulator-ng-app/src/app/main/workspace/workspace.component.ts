import {AfterContentChecked, AfterViewChecked, Component, Injectable, Input, OnInit} from '@angular/core';
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
  loading = false;

  routers : Router[] = [];
  links : Link[] = [];
  clients: Client[] = [];

  constructor(private apiService : ApiService) {}

  ngOnInit() {
    this.loading = true;
    this.getAllRouters();
    this.getAllClients();
  }

  //Dodaj nowy router do pola roboczego
  addRouter() {
    let router = new Router();
    this.apiService.postRouter(router).subscribe(
      response => {
        router.id = response.id;
        router.actualX = response.actualX;
        router.actualY = response.actualY;
        router.previousX = response.previousX;
        router.previousY = response.previousY;
        this.routers.push(router);
      },
      error => {
        alert("An error occured - Cannot add new router!");
      }
    );
  }

  // addClient() {
  //   let client = new Client();
  //   this.apiService.postClient(client).subscribe(
  //     response => {
  //       client.id = response.id;
  //       client.x = response.x;
  //       client.y = response.y;
  //       this.clients.push(client);
  //     },
  //     error => {
  //       alert("An error occured - Cannot add new client!");
  //     }
  //   );
  // }

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

  //Po każdym przeciągnięciu node'a uaktualniamy
  //jego pozycję i wysyłamy do bazy
  updateParameters(event, router) {
    let element = event.source.getRootElement();
    let boundingClientRect = element.getBoundingClientRect();
    let parentPosition = this.getPosition(element);

    router.previousX += router.actualX == undefined ? 0 : router.actualX;
    router.previousY += router.actualY == undefined ? 0 : router.actualY;

    router.actualX = boundingClientRect.x - parentPosition.left;
    router.actualY = boundingClientRect.y - parentPosition.top;

    this.apiService.postRouter(router).subscribe(
      response => {
      },
      error => {
        alert("An error occured - Cannot update node parameters!");
      }
    );
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
    this.apiService.deleteAllRouters().subscribe(
      response => {
        this.routers = response;
      },
      error => {
        alert("An error occured during workspace reset")
      }
    );

    // this.apiService.deleteAllClients().subscribe(
    //   response => {
    //     this.clients = response;
    //   },
    //   error => {
    //     alert("An error occured during workspace reset")
    //   }
    // );
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

  select(id: string) {
    for (let router of this.routers) {
      router.selected = router.id == id;
    }
  }

  removeSelectedRouter() {
    for(let router of this.routers) {
      if (router.selected) {
        this.apiService.deleteRouter(router.id).subscribe(
          response => {
            this.routers = response;
          },
          error => {
            alert("An error occured during removing router")
          }
        );
      }
    }
  }
}
