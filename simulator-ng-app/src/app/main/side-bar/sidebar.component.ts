import { Component, OnInit } from '@angular/core';
import {CdkDragDrop, moveItemInArray, transferArrayItem} from "@angular/cdk/drag-drop";
import {WorkspaceComponent} from "../workspace/workspace.component";

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {

    workspace: WorkspaceComponent;

    opened = true;

    constructor() { }

    ngOnInit() {
      this.workspace = new WorkspaceComponent();
    }

}
