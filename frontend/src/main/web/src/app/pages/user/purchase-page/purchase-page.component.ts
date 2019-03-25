import { Component, OnInit, OnDestroy } from '@angular/core';
import { FlightService } from '../../flights/shared/models/flight-service.model';
import { FlightClass } from '../../flights/shared/models/flight-class.model';
import { Trip } from '../../../shared/model/trip.model';
import { Router } from '@angular/router';
import { BookedTicket } from '../shared/model/bought_ticket.model';
import { CartService } from '../shared/service/cart.service';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'app-purchase-page',
  templateUrl: './purchase-page.component.html',
  styleUrls: ['./purchase-page.component.scss']
})
export class PurchasePageComponent implements OnInit, OnDestroy {

  tickets: BookedTicket[] = [];

  constructor(private router: Router,
              private cartService: CartService) {
    this.tickets = JSON.parse(sessionStorage.getItem('boughtTickets'));
    this.tickets.forEach(ticket => 
      ticket.totalPrice = this.getTotalPrice(ticket.services, ticket.ticket, ticket.amount)
    );
  }

  ngOnInit() { }

  ngOnDestroy(){
    for(let ticket of this.tickets){
      if(ticket.is_bought){
        this.delete(ticket);
      }
    }
  }

  getTotalPrice(services: FlightService[], ticket: FlightClass, amount: number): number{
    let servicesPrice = 0;
    services.forEach(service => {servicesPrice += service.service_price;});
    return amount*(ticket.ticket_price + servicesPrice);
  }

  delete(bookedTicket: BookedTicket){
    for(let i=0; i < this.tickets.length; i++){
      if(bookedTicket === this.tickets[i]){
        this.tickets.splice(i);
        sessionStorage.setItem('boughtTickets', JSON.stringify(this.tickets));
      }
    }
  }

  buy(ticket: BookedTicket){
    ticket.is_bought = true;
    // this.cartService.buyTicket().subscribe((resp: HttpResponse<any>) => ticket.is_bought = true)
  }
}
