import { Component, OnInit, OnDestroy } from '@angular/core';
import { FlightService } from '../../flights/shared/models/flight-service.model';
import { FlightClass } from '../../flights/shared/models/flight-class.model';
import { BookedTicket } from '../shared/model/bought_ticket.model';
import { CartService } from '../shared/service/cart.service';
import { HttpResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { DataService } from '../../../shared/data.service';
import {MessageService} from "primeng/api";
import {ShowMessageService} from "../../admin/approver/shared/service/show-message.service";

@Component({
  selector: 'app-purchase-page',
  templateUrl: './purchase-page.component.html',
  styleUrls: ['./purchase-page.component.scss']
})
export class PurchasePageComponent implements OnInit, OnDestroy {

  tickets: BookedTicket[] = [];
  btnBlock: boolean = false;

  constructor(private cartService: CartService,
              private router: Router,
              private dataService: DataService,
              private messageService: MessageService,
              private showMsgSrvc: ShowMessageService) {
    this.tickets = JSON.parse(sessionStorage.getItem('boughtTickets'));
    if(this.tickets !== null){
      this.tickets.forEach(ticket => 
        ticket.totalPrice = this.getTotalPrice(ticket.services, ticket.ticket, ticket.amount));
    }
  }

  ngOnInit() { }

  ngOnDestroy(){
    if (this.tickets !== null){
      for(let ticket of this.tickets){
        if(ticket.is_bought){
          this.delete(ticket);
        }
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
        this.tickets.splice(i, 1);
        sessionStorage.setItem('boughtTickets', JSON.stringify(this.tickets));
      }
    }
  }

  buy(ticket: BookedTicket){
    this.btnBlock = true;
    this.cartService.buyTicket(ticket).subscribe((resp: HttpResponse<any>) => {
      ticket.is_bought = true;
      this.btnBlock = false;
    }),error => {
      this.btnBlock = false;
      this.showMsgSrvc.showMessage(this.messageService,
        'error',
        `Sorry`,
        `This ticket is already bought`);
      this.delete(ticket);
    };
  }

  returnTicket(ticket: BookedTicket){
    this.dataService.sendFormData({startPlanet: ticket.trip.arrival_planet_name,
                                  finishPlanet: ticket.trip.departure_planet_name,
                                  startSpaceport: ticket.trip.arrival_spaceport_name,
                                  finishSpaceport: ticket.trip.departure_spaceport_name});
    this.router.navigate(['/flights']);
  }
}
