import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Router} from "../main/model/Router";
import {Client} from "../main/model/Client";

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private ROUTER_URL = 'http://localhost:8080/workspace/router';
  private CLIENT_URL = 'http://localhost:8080/workspace/client';
  private ALL_ROUTERS_URL = 'http://localhost:8080/workspace/router/all';
  private ALL_CLIENTS_URL = 'http://localhost:8080/workspace/client/all';

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

  deleteRouter(id : string) {
    return this.httpClient.delete<Router[]>("http://localhost:8080/workspace/router" + "/" + id);
  }

  postClient(client : Client) : Observable<Client> {
    return this.httpClient.post<Client>(this.CLIENT_URL, client);
  }

  deleteAllRouters() : Observable<Router[]> {
     return this.httpClient.delete<Router[]>(this.ALL_ROUTERS_URL);
  }

  deleteAllClients() : Observable<Client[]> {
    return this.httpClient.delete<Client[]>(this.ALL_CLIENTS_URL);
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
