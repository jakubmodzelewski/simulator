import {Component, ContentChildren, NgModule, OnInit} from '@angular/core';
import {CdkDragDrop, moveItemInArray, transferArrayItem} from "@angular/cdk/drag-drop";
import {WorkspaceComponent} from "../workspace/workspace.component";
import {ApiService} from "../../shared/api.service";
import {HttpClient} from "@angular/common/http";
import {BrowserModule} from "@angular/platform-browser";

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {

    opened = true;

    constructor(private workspace : WorkspaceComponent) { }

    ngOnInit() {
    }

}
