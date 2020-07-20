import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Node} from "../main/model/Node";
import {Router} from "../main/model/Router";
import {Client} from "../main/model/Client";

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private ROUTER_URL = 'http://localhost:8080/main/router';
  private CLIENT_URL = 'http://localhost:8080/main/client';
  private ALL_ROUTERS_URL = 'http://localhost:8080/main/router/all';
  private ALL_CLIENTS_URL = 'http://localhost:8080/main/client/all';

  constructor(private httpClient : HttpClient) { }

  getAllRouters() : Observable<Router[]> {
    return this.httpClient.get<Router[]>(this.ALL_ROUTERS_URL);
  }

  getAllClients() : Observable<Client[]> {
    return this.httpClient.get<Client[]>(this.ALL_CLIENTS_URL);
  }

  postRouter(router : Router) : Observable<Router> {
    return this.httpClient.post<Router>(this.ROUTER_URL, router);
  }

  postClient(client : Client) : Observable<Client> {
    return this.httpClient.post<Client>(this.CLIENT_URL, client);
  }

  resetWorkspace() {
    this.httpClient.delete(this.CLIENT_URL);
    this.httpClient.delete(this.ROUTER_URL);
  }

  //TODO Obsługa ping

  // pingRequest() {
  //   this.http.get(SIMULATION_URL).subscribe(); {
  //     result => {
  //
  //     };
  //   }
  // }
}
