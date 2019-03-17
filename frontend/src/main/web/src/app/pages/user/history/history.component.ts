import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {HistoryService} from "./history.service";
import {Servicehistorymodel} from "./servicehistorymodel.model";

@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.scss'],
})
export class HistoryComponent implements OnInit {

  public searchParams: FormGroup;
  public ticketData: any;

  public page: number = 1;
  public ticketsPerPage: number = 2;

  public beforeDate: string;
  public afterDate: string;

  constructor(private historyService: HistoryService) {
    this.searchParams = new FormGroup({
      beforeDate: new FormControl(""),
      afterDate: new FormControl(""),
    });
  }

  ngOnInit() {

    this.historyService.getUserTicketHistory(0, this.ticketsPerPage, undefined, undefined)
        .subscribe(data => {
          this.ticketData = data;
        });
  }

  search(data) {
    this.beforeDate = data.beforeDate;
    this.afterDate = data.afterDate;
    this.historyService.getUserTicketHistory(0, this.ticketsPerPage, data.beforeDate, data.afterDate)
        .subscribe(data => {
          this.ticketData = data;
        });
  }

  onPageChange(page) {
    this.page = page;

    let requestPage = page == 1 ? 0 : page - 1;

    this.historyService.getUserTicketHistory(
      requestPage * this.ticketsPerPage,
      this.ticketsPerPage,
      this.beforeDate,
      this.afterDate
    )
        .subscribe(data => {
          this.ticketData = data;
        });
  }

  loadServices(ticket) {
    if (ticket.services != undefined) {
      return;
    }
    this.historyService.getTicketServices(ticket.id)
        .subscribe(services => {
          ticket.services = services;
          if (ticket.services.length == 0) {
            ticket.services[0] = new Servicehistorymodel("No services");
          }
        });
  }

}
