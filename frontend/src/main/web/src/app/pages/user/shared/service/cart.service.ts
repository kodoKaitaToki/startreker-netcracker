import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Api } from '../../../../modules/api';

@Injectable({providedIn: 'root'})
export class CartService{

    constructor(private http: HttpClient){}

    buyTicket(){
        return this.http.get(Api.user.buyTicket(), Api.HttpOptionsAuthorized);
    }
}  