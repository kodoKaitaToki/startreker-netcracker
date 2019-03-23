import {Component, Input, OnInit} from '@angular/core';
import {FlightClass} from "../../shared/models/flight-class.model";
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {SearchService} from "../../shared/services/search.service";
import {TicketClassInfoComponent} from "./ticket-class-info/ticket-class-info.component";

@Component({
  selector: 'app-ticket-class',
  templateUrl: './ticket-class.component.html',
  styleUrls: ['./ticket-class.component.scss'],
})
export class TicketClassComponent implements OnInit {

  @Input('ticket_class') ticket_class: FlightClass;

  constructor(private searchSvc: SearchService,
              private modalService: NgbModal) {
  }

  ngOnInit() {
  }

  openVerticallyCentered() {
    const modalRef = this.modalService.open(TicketClassInfoComponent, {centered: true, size: 'lg'});
    modalRef.componentInstance.ticket_class = this.ticket_class;
  }
}
