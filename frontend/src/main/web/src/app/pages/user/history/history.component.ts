import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {HistoryService} from "../shared/service/history.service";
import {Servicehistorymodel} from "../shared/model/servicehistorymodel.model";
import {HttpErrorResponse} from "@angular/common/http";
import {MessageService} from "primeng/api";
import {ShowMessageService} from "../../admin/approver/shared/service/show-message.service";
import {periodError} from "../../../shared/dateValidator";

@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.scss'],
})
export class HistoryComponent implements OnInit {

  ticketData: any;

  page: number = 1;
  ticketsPerPage: number = 5;
  totalItems: number;

  currentFromDate: string;
  currentToDate: string;

  locked: boolean = false;

  errorCallback = (error: HttpErrorResponse) => {
    this.locked = false;
    this.showMsgSrvc.showMessage(this.messageService, 'error', `Error message - ${error.error.status}`,
      error.error.error
    );
  };

  totalItemsCallback = (totalItems: number) => {
    this.totalItems = totalItems;
  };

  searchParams = this.fb.group({
    fromDate: ['', Validators.required],
    toDate: ['', Validators.required],
  }, {validator: periodError("fromDate", "toDate")});


  constructor(private historyService: HistoryService,
    private messageService: MessageService,
    private showMsgSrvc: ShowMessageService, private fb: FormBuilder) {

    this.historyService.totalItemsCallback = this.totalItemsCallback;
  }

  ngOnInit() {

    this.historyService.getUserTicketHistory(0, this.ticketsPerPage, undefined, undefined)
        .subscribe(data => {
          this.ticketData = data;
        }, this.errorCallback);
  }

  search() {
    if (this.locked) {
      return;
    }

    this.currentFromDate = this.searchParams.value.fromDate;
    this.currentToDate = this.searchParams.value.toDate;

    this.locked = true;

    this.historyService.getUserTicketHistory(0, this.ticketsPerPage, this.currentFromDate,this.currentToDate)
        .subscribe(data => {
          this.page = 1;
          this.ticketData = data;
          this.locked = false;
        }, this.errorCallback);
  }

  onPageChange(page) {

    if (this.locked) {
      return;
    }

    this.locked = true;

    let requestPage = page == 1 ? 0 : page - 1;

    this.historyService.getUserTicketHistory(
      requestPage * this.ticketsPerPage,
      this.ticketsPerPage,
      this.currentFromDate,
     this.currentToDate
    )
        .subscribe(data => {
          this.page = page;
          this.ticketData = data;
          window.scroll(0, 0);
          this.locked = false;
        }, this.errorCallback);
  }

  loadServices(ticket) {
    if (ticket.services != undefined) {
      return;
    }
    this.historyService.getTicketServices(ticket.id)
        .subscribe(services => {
          ticket.services = services;
          if (ticket.services.length == 0) {
            ticket.services[0] = new Servicehistorymodel("No services", 0);
          }
        }, this.errorCallback);
  }
}
