import { Component, OnInit } from '@angular/core';
import {Router} from "../model/Router";

@Component({
  selector: 'app-workspace',
  templateUrl: './workspace.component.html',
  styleUrls: ['./workspace.component.css']
})
export class WorkspaceComponent implements OnInit {

  routers : Router[] = [];

  constructor() { }

  ngOnInit() {
  }

  addRouter() {
    this.routers.push(new Router());
    console.log(this.routers.length);
  }

}
