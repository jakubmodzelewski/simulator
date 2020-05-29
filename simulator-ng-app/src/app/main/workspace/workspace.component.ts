import {Component, Inject, Injectable, Input, OnInit} from '@angular/core';
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

  @Input()
  routers : Router[] = [];
  links : Link[] = [];
  clients: Client[] = [];

  constructor(private apiService : ApiService) {}

  ngOnInit() {
    this.getAllRouters();
  }

  //Dodaj nowy router do pola roboczego
  addRouter() {
    let router = new Router();
    this.apiService.postRouter(router).subscribe(
      //Przypisuje id pobrane z serwera i dodaje nowy router do listy routerów
      response => {
        router.id = response.id;
        this.routers.push(router);
        console.log(this.routers.length);
      },
      error => {
        alert("An error occured - Cannot add new router!");
      }
    );
    window.location.reload();
  }

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

}
