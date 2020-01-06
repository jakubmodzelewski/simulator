import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavigationComponent } from './navigation/navigation.component';
import { HelpComponent } from './help/help.component';
import { MainComponent } from './main/main.component';
import { NotFoundComponent } from './not-found/not-found.component';
import {Router, RouterModule, Routes} from "@angular/router";

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
    NotFoundComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    RouterModule.forRoot(appRoutes, {enableTracing:true}) //shows Router Events
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
