import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavigationComponent } from './navigation/navigation.component';
import { HelpComponent } from './help/help.component';
import { MainComponent } from './main/main.component';
import { NotFoundComponent } from './not-found/not-found.component';
import { RouterModule, Routes} from "@angular/router";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatSidenavModule} from "@angular/material/sidenav";
import {DragDropModule} from "@angular/cdk/drag-drop";
import {WorkspaceComponent} from "./main/workspace/workspace.component";
import {HttpClientModule} from "@angular/common/http";

const appRoutes :Routes = [
  {
    path:'main',
    component:MainComponent
  },
  {
    path:'help',
    component:HelpComponent
  },
  {
    path:'',
    component:MainComponent,
    pathMatch:'full'
  },
  {
    path:'**',
    component:NotFoundComponent
  }

]

@NgModule({
  declarations: [
    AppComponent,
    NavigationComponent,
    HelpComponent,
    MainComponent,
    NotFoundComponent,
    WorkspaceComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    RouterModule.forRoot(appRoutes, {enableTracing: false}),
    BrowserAnimationsModule,
    MatSidenavModule,
    DragDropModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
