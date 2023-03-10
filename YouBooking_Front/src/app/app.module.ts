import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {HttpClient, HttpClientModule} from "@angular/common/http";
import {FormsModule} from "@angular/forms";
import { AuthenticationComponent } from './components/authentication/authentication.component';
import {NgxPaginationModule} from "ngx-pagination";
import {JWT_OPTIONS, JwtHelperService, JwtModule} from "@auth0/angular-jwt";
import {AuthService} from "./services/auth.service";
import { SignupComponent } from './components/signup/signup.component';
import {NavBarComponent} from "./components/nav-bar/nav-bar.component";
import {HomeComponent} from "./components/home/home.component";
import {FooterComponent} from "./components/footer/footer.component";

@NgModule({
  declarations: [
    AppComponent,
    AuthenticationComponent,
    SignupComponent,
    HomeComponent,
    NavBarComponent,
    FooterComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    NgxPaginationModule,
    JwtModule.forRoot({
      jwtOptionsProvider: {
        provide: JWT_OPTIONS,
        useValue: {
          tokenGetter: () => {
            // return the access token from storage
            return localStorage.getItem('access_token');
          },
          // other options you want to pass to the JWT module
        }
      }
    })
  ],
  providers: [AuthService],
  bootstrap: [AppComponent]
})
export class AppModule { }
