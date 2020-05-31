import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Node} from "../main/model/Node";

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private NODE_URL = 'http://localhost:8080/main/node';
  private ALL_NODES_URL = 'http://localhost:8080/main/node/all';

  constructor(private httpClient : HttpClient) { }

  getAllNodes() : Observable<Node[]> {
    return this.httpClient.get<Node[]>(this.ALL_NODES_URL);
  }

  //Dodaje nowy węzeł lub uaktualnia jego stan
  postNode(node : Node) : Observable<Node> {
    return this.httpClient.post<Node>(this.NODE_URL, node);
  }

  resetWorkspace() {
    return this.httpClient.delete(this.NODE_URL);
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
