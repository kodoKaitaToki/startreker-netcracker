import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { clone } from 'ramda';

import { Api, HttpOptions, HttpOptionsAuthorized } from '../modules/api';
import { LoginFormData } from './interfaces/login-form.interface';
import { RegisterFormData } from './interfaces/registration-form.interface';
import { RegisterResponse } from './interfaces/register-response.interface';
import { LoginResponse } from './interfaces/login-response.interface';

@Injectable({
  providedIn: 'root'
})

export class ApiUserService {
  userData = {
    username: '',
    email: '',
    phone: '',
    authorities: []
  }

  constructor(
    private http: HttpClient,
    private router: Router
    ) {}


    get userInfo() {
      return this.userData;
    }

    
    // loginUser(userData: LoginFormData) {
    //   this.http.post<any>(Api.auth.loginUser(), userData, HttpOptions)
    //     .subscribe(
    //       (userData: LoginResponse) => {
    //         this.userData = clone(userData);
    //         localStorage.setItem('at', userData.access_token);
    //         localStorage.setItem('rt', userData.refresh_token);
    //       },
    //       error => console.error(error)
    //     );
    // }

    loginUser(userData: LoginFormData){
      return this.http.post<any>(Api.auth.loginUser(), userData, HttpOptions);
    }

    getLoggedUser(userData: LoginResponse){
      this.userData = clone(userData);
      localStorage.setItem('at', userData.access_token);
      localStorage.setItem('rt', userData.refresh_token);
    }

    // async loginUser(userData: LoginFormData) {
    //   try {
    //     console.log(userData);
    //     const data: any = await this.http.post(Api.auth.loginUser(), userData, HttpOptions).toPromise();
    //     this.userData = clone(data);
    //     localStorage.setItem('at', data.access_token);
    //     localStorage.setItem('rt', data.refresh_token);
    //   } catch (error) {
    //     console.error(error);
    //   }
    // }

    // async registerUser(userData: LoginFormData) {
    //   try {
    //     console.log(userData);
    //     const data: any = await this.http.post(Api.auth.loginUser(), userData, HttpOptions).toPromise();
    //     this.userData = clone(data);
    //   } catch (error) {
    //     console.error(error);
    //   }
    // }

    public registerUser(userData: RegisterFormData){
      return this.http.post<any>(Api.auth.registerUser(), userData, HttpOptions)
    }

    public getRegisteredUser(userData){
      this.userData = userData;
    }


    /*registerUser(userData: RegisterFormData) {
      this.http.post<any>(Api.auth.registerUser(), userData, HttpOptions)
        .subscribe(
          (userData: RegisterResponse) => {
            this.userData = clone(userData);
          },
          error => console.error(error)
        );
    }*/

    logoutUser() {
      this.http.post<any>(Api.auth.logoutUser(), {}, HttpOptionsAuthorized);
      localStorage.removeItem('at');
      localStorage.removeItem('rt');
      this.userData = clone({});
      window.location.href = 'http://127.0.0.1:4200';
    }

    async recoverPassword(userData: any) {
      try {
        console.log(userData);
        const data: any = await this.http.post(Api.auth.recoverPassword(), userData, HttpOptions).toPromise();
        this.userData = clone(data);
      } catch (error) {
          console.error(error);
        }
    }
  }