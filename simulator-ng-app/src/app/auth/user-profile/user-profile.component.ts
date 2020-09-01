import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Simulation} from "../../main/model/Simulation";
import {ApiService} from "../../shared/api.service";
import {AuthService} from "../shared/auth.service";

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {

  name: string;
  simulations: Simulation[];

  constructor(private activatedRoute: ActivatedRoute, private apiService : ApiService, private authService : AuthService) {
  }

  ngOnInit(): void {
    this.name = this.authService.getUserName();
    this.apiService.getUserSimulations(this.name).subscribe(
      response => {
        this.simulations = response;
      },
      err => {
        alert("An error occured when getting user simulations from the server!")
      }
    );
  }

  loadSimulation(id: string) {
    this.apiService.setActiveSimulationId(id);
  }
}
