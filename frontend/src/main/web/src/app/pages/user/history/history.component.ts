import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {HistoryService} from "./history.service";
import {Servicehistorymodel} from "./servicehistorymodel.model";
import {HttpErrorResponse} from "@angular/common/http";
import {MessageService} from "primeng/api";
import {ShowMessageService} from "../../admin/approver/shared/service/show-message.service";

@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.scss'],
})
export class HistoryComponent implements OnInit {

  public searchParams: FormGroup;
  public ticketData: any;

  public page: number = 1;
  public ticketsPerPage: number = 3;
  public totalItems: number;

  public beforeDate: string;
  public afterDate: string;

  public locked: boolean = false;

  errorCallback = (error: HttpErrorResponse) => {
    this.locked = false;
    this.showMsgSrvc.showMessage(this.messageService, 'error', `Error message - ${error.error.status}`,
      error.error.error
    );
  };

  totalItemsCallback = (totalItems: number) => {
    this.totalItems = totalItems;
  };

  constructor(private historyService: HistoryService,
    private messageService: MessageService,
    private showMsgSrvc: ShowMessageService) {
    this.searchParams = new FormGroup({
      beforeDate: new FormControl(""),
      afterDate: new FormControl(""),
    });
    this.historyService.totalItemsCallback = this.totalItemsCallback;
  }

  ngOnInit() {

    this.historyService.getUserTicketHistory(0, this.ticketsPerPage, undefined, undefined)
        .subscribe(data => {
          this.ticketData = data;
        }, this.errorCallback);
  }

  search(data) {
    this.beforeDate = data.beforeDate;
    this.afterDate = data.afterDate;

    if (!this.validateDates(this.beforeDate, this.afterDate)) {
      return
    }

    if (this.locked) {
      return;
    }

    this.locked = true;

    this.historyService.getUserTicketHistory(0, this.ticketsPerPage, data.beforeDate, data.afterDate)
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
      this.beforeDate,
      this.afterDate
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

  validateDates(fromDate: string, toDate: string): boolean {
    if (!(HistoryComponent.isBlank(fromDate) == HistoryComponent.isBlank(toDate))) {
      this.showMsgSrvc.showMessage(this.messageService,
        'error',
        `Incorrect date`,
        `Please enter both dates, or no dates at all to search through all of history`
      );
      return false;

    }

    if (fromDate > toDate) {
      this.showMsgSrvc.showMessage(this.messageService,
        'error',
        `Incorrect date`,
        `Starting date is bigger than ending date`
      );
      return false;
    }

    return true;
  }

  static isBlank(str: string) {
    return (!str || str.length === 0);
  }
}
