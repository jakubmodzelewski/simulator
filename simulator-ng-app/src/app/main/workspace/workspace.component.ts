import {Component, Inject, Injectable, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {Router} from "../model/Router";
import {Link} from "../model/Link";
import {Client} from "../model/Client";
import {ApiService} from "../../shared/api.service";
import {CdkDragEnd} from "@angular/cdk/drag-drop";

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
  rightSideBarOpened = true;

  @Input()
  routers : Router[] = [];
  links : Link[] = [];
  clients: Client[] = [];

  constructor(private apiService : ApiService) {}

  ngOnInit() {
    this.getAllNodes();
  }

  //Dodaj nowy router do pola roboczego
  addRouter() {
    let router = new Router();
    this.apiService.postNode(router).subscribe(
      //Przypisuje id pobrane z serwera i dodaje nowy router do listy routerów
      response => {
        router.id = response.id;
        router.x = response.x;
        router.y = response.y;
        this.routers.push(router);
      },
      error => {
        alert("An error occured - Cannot add new node!");
      }
    );
  }

  //Pobierz z serwera wszystkie node'y
  getAllNodes() {
    this.apiService.getAllNodes().subscribe(
      response => {
        this.routers = response;
      },
      err => {
        alert("An error occured when getting nodes from the server!")
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

    this.apiService.postNode(node).subscribe(
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
    this.apiService.resetWorkspace().subscribe(
      response => {},
      error => {
        alert("An error occured - Cannot reset workspace!")
      }
    )
    this.routers = [];
  }
}
