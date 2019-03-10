import {Component, OnInit} from '@angular/core';
import {CarrierDiscountsService} from "../shared/service/carrier-discount.service";
import {Suggestion} from "../shared/model/suggestion.model";
import {Trip} from "../shared/model/trip.model";

@Component({
  selector: 'app-discount-main-page',
  templateUrl: './discount-main-page.component.html',
  styleUrls: ['./discount-main-page.component.scss'],
})
export class DiscountMainPageComponent implements OnInit {

  serviceTypesForDiscount = [
    'class-ticket',
    'suggestion'
  ];

  selects = [
    'Ticket class',
    'Suggestion'
  ];

  currentTypeDataToRender = '';

  tripsWithTicketClasses: Trip[] = [];

  tripsWithSuggestions: Trip[] = [];

  constructor(private ticketClassDiscountSrvc: CarrierDiscountsService) {
  }

  ngOnInit() {

    this.ticketClassDiscountSrvc.setServiceTypeForDiscount(`${this.serviceTypesForDiscount[0]}/discount`);
    this.currentTypeDataToRender = this.selects[0];

    this.getTicketClasses();
  }

  setDiscountType(value) {

    if (value === 'Ticket class') {
      this.ticketClassDiscountSrvc.setServiceTypeForDiscount(`${this.serviceTypesForDiscount[0]}/discount`);
      this.getTicketClasses();
    }

    if (value === 'Suggestion') {
      this.ticketClassDiscountSrvc.setServiceTypeForDiscount(`${this.serviceTypesForDiscount[1]}/discount`);
      this.getSuggestions();
    }

    this.currentTypeDataToRender = value;
  }

  getTicketClasses() {

    this.ticketClassDiscountSrvc.buildApiForGetAndPost();

    this.ticketClassDiscountSrvc.getAll()
        .subscribe((data) => {
          this.tripsWithTicketClasses = data;
        }, (error) => {
          console.log(error);
        });
  }

  getSuggestions() {

    this.ticketClassDiscountSrvc.buildApiForGetAndPost();

    this.ticketClassDiscountSrvc.getAll()
        .subscribe((data) => {
          this.tripsWithSuggestions = data;
        });
  }

  postTicketClassDiscount($event) {

    this.ticketClassDiscountSrvc.buildApiForGetAndPost();

    this.ticketClassDiscountSrvc.post($event)
        .subscribe(() => {
          this.getTicketClasses();
        }, (error) => {
          console.log(error);
        });
  }

  postSuggestionDiscount($event) {

    this.ticketClassDiscountSrvc.buildApiForGetAndPost();

    this.ticketClassDiscountSrvc.post($event)
        .subscribe(() => {
          this.getSuggestions();
        }, (error) => {
          console.log(error);
        });
  }

  deleteTicketClassDiscount($event) {

    this.ticketClassDiscountSrvc.buildApiForDelete($event.discount.discount_id);

    this.ticketClassDiscountSrvc.delete()
        .subscribe(() => {
          this.getTicketClasses();
        }, (error) => {
          console.log(error);
        });
  }

  deleteSuggestionDiscount($event) {

    this.ticketClassDiscountSrvc.buildApiForDelete($event.discount.discount_id);

    this.ticketClassDiscountSrvc.delete()
        .subscribe(() => {
          this.getSuggestions();
        }, (error) => {
          console.log(error);
        });
  }
}
