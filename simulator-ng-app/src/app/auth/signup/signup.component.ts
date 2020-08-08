import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {SignupRequestPayload} from "./signup-request-payload";
import {AuthService} from "../shared/auth.service";
import {ToastrService} from "ngx-toastr";
import {Router} from "@angular/router";

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

  signupRequestPayload: SignupRequestPayload;
  signUpForm: FormGroup;

  constructor(private authService : AuthService, private router : Router, private toastrService : ToastrService) {
    this.signupRequestPayload = {
      username: '',
      email: '',
      password: ''
    }
  }

  ngOnInit() {
    this.signUpForm = new FormGroup({
      username: new FormControl('', Validators.required),
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', Validators.required),

    });
  }

  signUp() {
    this.signupRequestPayload.username = this.signUpForm.get('username').value;
    this.signupRequestPayload.email = this.signUpForm.get('email').value;
    this.signupRequestPayload.password = this.signUpForm.get('password').value;

    this.authService.signup(this.signupRequestPayload)
      .subscribe(() => {
        this.router.navigate(['/login'],
          { queryParams: { registered: 'true' } });
      }, error => {
        this.toastrService.error('Registration failure');
      });
  }
}
