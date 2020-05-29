import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Router} from "../main/model/Router";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private ROUTER_URL = 'http://localhost:8080/main/router';
  private ALL_ROUTERS_URL = 'http://localhost:8080/main/router/all';

  constructor(private httpClient : HttpClient) { }

  getAllRouters() : Observable<Router[]> {
    return this.httpClient.get<Router[]>(this.ALL_ROUTERS_URL);
  }

  //Dodaje nowy router lub uaktualnia jego stan
  postRouter(router : Router) : Observable<Router> {
    return this.httpClient.post<Router>(this.ROUTER_URL, router);
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
