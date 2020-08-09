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
import { SignupComponent } from './auth/signup/signup.component';
import { LoginComponent } from './auth/login/login.component';
import {ReactiveFormsModule} from "@angular/forms";
import {NgxWebstorageModule} from "ngx-webstorage";
import {ToastrModule} from "ngx-toastr";
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";
import {UserProfileComponent} from "./auth/user-profle/user-profile.component";

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
    path:'signup',
    component:SignupComponent,
  },
  {
    path:'login',
    component:LoginComponent,
  },
  {
    path:'**',
    component:NotFoundComponent
  },
  {
    path:'user-profile/:name',
    component:UserProfileComponent
  }

]

@NgModule({
  declarations: [
    AppComponent,
    NavigationComponent,
    HelpComponent,
    MainComponent,
    NotFoundComponent,
    WorkspaceComponent,
    SignupComponent,
    LoginComponent,
    UserProfileComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    RouterModule.forRoot(appRoutes, {enableTracing: false}),
    BrowserAnimationsModule,
    MatSidenavModule,
    DragDropModule,
    HttpClientModule,
    ReactiveFormsModule,
    NgxWebstorageModule.forRoot(),
    BrowserAnimationsModule,
    ToastrModule.forRoot(),
    NgbModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
