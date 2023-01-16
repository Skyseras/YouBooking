import { Component, OnInit } from '@angular/core';
import {Users} from "../../models/users";
import {HttpErrorResponse} from "@angular/common/http";
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {
  user:Users;
  confirmPassword!:String;
  role:String="CLIENT";
  errorMessage:String="";
  successMessage:String="";

  constructor(private authService:AuthService,private router:Router) {
    this.user = new Users();
  }

  ngOnInit(): void {
  }

  onSubmit(){
    console.log(this.role)
    if(this.user.name == null){
      this.errorMessage = "Needs name";
    }
    else if(this.user.username==null || this.user.password == null){
      this.errorMessage = this.user.username==null ? "Needs username":
        (this.user.password==null ? "Needs password" : "");
    }
    else if(this.confirmPassword == null){
      this.errorMessage = "Needs confirm password";
    }
    else if(this.user.password !== this.confirmPassword){
      this.errorMessage = "password and confirm password incorrect";
    }
    else{
      this.authService.role=this.role;
      this.authService.signUp(this.user).subscribe(
        (response) => {
          if (response instanceof HttpErrorResponse) {
            this.errorMessage = response.error;
          } else {
            this.errorMessage = "";
            this.successMessage = "Account created successfully";
            setTimeout(() => {
              this.successMessage = '';
              this.router.navigate(['/login']);
            }, 2500);
          }
        },
        (error) => {
          this.errorMessage = error;
        }
      );
    }
  }

}
