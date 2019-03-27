import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Api } from '../../../../modules/api';
import { BookedTicket } from '../model/bought_ticket.model';

@Injectable({providedIn: 'root'})
export class CartService{

    constructor(private http: HttpClient){}

    buyTicket(ticket: BookedTicket){
        let servIds: number[] = [];
        ticket.services.forEach(service => servIds.push(service.id))
        let body = {class_id: ticket.ticket.class_id,
                    p_services_ids: servIds}
        return this.http.post(Api.user.buyTicket(), body, Api.HttpOptionsAuthorized);
    }
}  