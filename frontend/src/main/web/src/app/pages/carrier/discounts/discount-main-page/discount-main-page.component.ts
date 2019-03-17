import {Component, OnInit} from '@angular/core';
import {CarrierDiscountsService} from "../shared/service/carrier-discount.service";
import {Suggestion} from "../shared/model/suggestion.model";
import {Trip} from "../shared/model/trip.model";
import {MessageService} from "primeng/api";
import {ShowMessageService} from "../../../admin/approver/shared/service/show-message.service";
import {HttpErrorResponse} from "@angular/common/http";
import { clone } from 'ramda';
import { HttpResponse } from '@angular/common/http';
import {
  suggestion_add_discount_success_msg,
  suggestion_delete_discount_success_msg,
  suggestion_info_msg,
  suggestion_sucess_list_msg,
  ticket_class_add_discount_success_msg,
  ticket_class_delete_discount_success_msg,
  ticket_class_info_msg,
  ticket_class_success_list_msg,
} from "../shared/message-consts";
import { checkToken } from '../../../../modules/api';

@Component({
  selector: 'app-discount-main-page',
  templateUrl: './discount-main-page.component.html',
  styleUrls: ['./discount-main-page.component.scss'],
  providers: [ShowMessageService]
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

  constructor(private ticketClassDiscountSrvc: CarrierDiscountsService,
    private msgSrv: MessageService,
    private showMsgSrvc: ShowMessageService) {
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

    this.showMsgSrvc.showMessage(this.msgSrv, 'info', ticket_class_info_msg.summary, ticket_class_info_msg.detail);

    this.ticketClassDiscountSrvc.getAll()
        .subscribe((resp: HttpResponse<any>) => {
          checkToken(resp.headers);
          this.tripsWithTicketClasses = clone(resp.body);
          this.showMsgSrvc.showMessage(this.msgSrv, 'success', ticket_class_success_list_msg.summary,
            ticket_class_success_list_msg.detail);
        }, (error: HttpErrorResponse) => {
          this.showMsgSrvc.showMessage(this.msgSrv, 'error', `Error message - ${error.error.status}`,
            error.error.error);
        });
  }

  getSuggestions() {

    this.ticketClassDiscountSrvc.buildApiForGetAndPost();

    this.showMsgSrvc.showMessage(this.msgSrv, 'info', suggestion_info_msg.summary, suggestion_info_msg.detail);

    this.ticketClassDiscountSrvc.getAll()
        .subscribe((resp: HttpResponse<any>) => {
          checkToken(resp.headers);
          this.tripsWithSuggestions = clone(resp.body);
          this.showMsgSrvc.showMessage(this.msgSrv, 'success', suggestion_sucess_list_msg.summary,
            suggestion_sucess_list_msg.detail);
        }, (error: HttpErrorResponse) => {
          this.showMsgSrvc.showMessage(this.msgSrv, 'error', `Error message - ${error.error.status}`,
            error.error.error);
        });
  }

  postTicketClassDiscount($event) {

    this.ticketClassDiscountSrvc.buildApiForGetAndPost();

    this.ticketClassDiscountSrvc.post($event)
        .subscribe((resp: HttpResponse<any>) => {
          checkToken(resp.headers);
          this.showMsgSrvc.showMessage(this.msgSrv, 'success', ticket_class_add_discount_success_msg.summary,
            ticket_class_add_discount_success_msg.detail);
          this.getTicketClasses();
        }, (error: HttpErrorResponse) => {
          this.showMsgSrvc.showMessage(this.msgSrv, 'error', `Error message - ${error.error.status}`,
            error.error.error);
        });
  }

  postSuggestionDiscount($event) {

    this.ticketClassDiscountSrvc.buildApiForGetAndPost();

    this.ticketClassDiscountSrvc.post($event)
        .subscribe((resp: HttpResponse<any>) => {
          checkToken(resp.headers);
          this.showMsgSrvc.showMessage(this.msgSrv, 'success', suggestion_add_discount_success_msg.summary,
            suggestion_add_discount_success_msg.detail);
          this.getSuggestions();
        }, (error: HttpErrorResponse) => {
          this.showMsgSrvc.showMessage(this.msgSrv, 'error', `Error message - ${error.error.status}`,
            error.error.error);
        });
  }

  deleteTicketClassDiscount($event) {

    this.ticketClassDiscountSrvc.buildApiForDelete($event.discount.discount_id);

    this.ticketClassDiscountSrvc.delete()
        .subscribe((resp: HttpResponse<any>) => {
          checkToken(resp.headers);
          this.showMsgSrvc.showMessage(this.msgSrv, 'success', ticket_class_delete_discount_success_msg.summary,
            ticket_class_delete_discount_success_msg.detail);
          this.getTicketClasses();
        }, (error: HttpErrorResponse) => {
          this.showMsgSrvc.showMessage(this.msgSrv, 'error', `Error message - ${error.error.status}`,
            error.error.error);
        });
  }

  deleteSuggestionDiscount($event) {

    this.ticketClassDiscountSrvc.buildApiForDelete($event.discount.discount_id);

    this.ticketClassDiscountSrvc.delete()
        .subscribe((resp: HttpResponse<any>) => {
          checkToken(resp.headers);
          this.showMsgSrvc.showMessage(this.msgSrv, 'success', suggestion_delete_discount_success_msg.summary,
            suggestion_delete_discount_success_msg.detail);
          this.getSuggestions();
        }, (error: HttpErrorResponse) => {
          this.showMsgSrvc.showMessage(this.msgSrv, 'error', `Error message - ${error.error.status}`,
            error.error.error);
        });
  }
}
