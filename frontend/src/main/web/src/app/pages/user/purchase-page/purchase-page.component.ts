import { Component, OnInit, OnDestroy } from '@angular/core';
import { BookedTicket } from '../shared/model/bought_ticket.model';
import { CartService } from '../shared/service/cart.service';
import { HttpResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { DataService } from '../../../shared/data.service';
import {MessageService} from "primeng/api";
import {ShowMessageService} from "../../admin/approver/shared/service/show-message.service";
import { Bundle } from '../../../shared/model/bundle';

@Component({
  selector: 'app-purchase-page',
  templateUrl: './purchase-page.component.html',
  styleUrls: ['./purchase-page.component.scss']
})
export class PurchasePageComponent implements OnInit, OnDestroy {

  tickets: BookedTicket[] = [];
  bundles: Bundle[] = [];
  bundlesIds: number[] = [];
  btnBlock: boolean = false;

  constructor(private cartService: CartService,
              private router: Router,
              private dataService: DataService,
              private messageService: MessageService,
              private showMsgSrvc: ShowMessageService) {}

  ngOnInit() { 
    this.bundlesIds = JSON.parse(sessionStorage.getItem('bundles'));
    if(this.bundlesIds !== null){
      for(let id of this.bundlesIds){
        this.cartService.getBundle(id).subscribe((resp: HttpResponse<any>) => {
          this.bundles.push(resp.body);
        })
      }
    }
    this.tickets = JSON.parse(sessionStorage.getItem('boughtTickets'));  
  }

  ngOnDestroy(){
    if (this.tickets !== null){
      for(let ticket of this.tickets){
        if(ticket.is_bought){
          this.delete(ticket);
        }
      }
    }
  }

  delete(bookedTicket: BookedTicket){
    for(let i=0; i < this.tickets.length; i++){
      if(bookedTicket === this.tickets[i]){
        this.tickets.splice(i, 1);
        sessionStorage.setItem('boughtTickets', JSON.stringify(this.tickets));
      }
    }
  }

  deleteBundle(bundle: Bundle){
    for(let i=0; i < this.bundles.length; i++){
      if(bundle === this.bundles[i]){
        this.bundles.splice(i, 1);
        this.bundlesIds.splice(i, 1);
        sessionStorage.setItem('bundles', JSON.stringify(this.bundlesIds));
      }
    }
  }

  buy(ticket){
    this.btnBlock = true;
    this.cartService.buyTicket(ticket).subscribe((resp: HttpResponse<any>) => {
      if(ticket instanceof BookedTicket){
        ticket.is_bought = true;
      }
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
