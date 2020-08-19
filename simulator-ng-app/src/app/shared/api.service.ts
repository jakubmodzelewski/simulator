import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Node} from "../main/model/Node";
import {Link} from "../main/model/Link";

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private NODE_URL = 'http://localhost:8080/workspace/node';
  private LINK_URL = 'http://localhost:8080/workspace/link';
  private ALL_NODES_URL = 'http://localhost:8080/workspace/node/all';
  private ALL_LINKS_URL = 'http://localhost:8080/workspace/link/all';

  constructor(private httpClient : HttpClient) { }

  getAllNodes() : Observable<Node[]> {
    return this.httpClient.get<Node[]>(this.ALL_NODES_URL);
  }

  getAllLinks() : Observable<Link[]> {
    return this.httpClient.get<Link[]>(this.ALL_LINKS_URL);
  }

  postNode(node : Node) : Observable<Node> {
    return this.httpClient.post<Node>(this.NODE_URL, node);
  }

  deleteNode(id : string) {
    return this.httpClient.delete<Node[]>("http://localhost:8080/workspace/node/" + id);
  }

  postLink(link : Link) : Observable<Link> {
    return this.httpClient.post<Link>(this.LINK_URL, link);
  }

  deleteAllNodes() : Observable<Node[]> {
     return this.httpClient.delete<Node[]>(this.ALL_NODES_URL);
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
