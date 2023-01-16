import { Component, OnInit } from '@angular/core';
import {Users} from "../../models/users";
import {AuthService} from "../../services/auth.service";
import {HttpErrorResponse} from "@angular/common/http";
import {Router} from "@angular/router";
import {JwtHelperService, JwtModule} from "@auth0/angular-jwt";

@Component({
  selector: 'app-authentication',
  templateUrl: './authentication.component.html',
  styleUrls: ['./authentication.component.css']
})
export class AuthenticationComponent implements OnInit {
  errorMessage:String="";
  successMessage:String="";
  user:Users;
  jwt!:any;
  constructor(private authService:AuthService, private router:Router) {
    this.user=new Users();
  }

  ngOnInit(): void {
    if(this.authService.isLogedIn()){
      this.router.navigate(['/']);
    }
  }

  onSubmit(){
  console.log(this.user)
    if(this.user.username==null || this.user.password == null){
      this.errorMessage = this.user.username==null ? "needs username":
        (this.user.password==null ? "needs password" : "");
    }
    else{
      this.authService.signIn(this.user).subscribe(
        (response) => {
          if (response instanceof HttpErrorResponse) {
            this.errorMessage = response.error.error;
            console.log(this.errorMessage)
          } else {
            this.errorMessage = "";
            this.successMessage = "You are connected";
            setTimeout(() => {
              this.successMessage = '';
              this.router.navigate(['/']);
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
