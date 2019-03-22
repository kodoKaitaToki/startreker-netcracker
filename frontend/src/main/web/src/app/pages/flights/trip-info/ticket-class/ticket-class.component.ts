import {Component, Input, OnInit} from '@angular/core';
import {FlightClass} from "../../shared/models/flight-class.model";
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-ticket-class',
  templateUrl: './ticket-class.component.html',
  styleUrls: ['./ticket-class.component.scss']
})
export class TicketClassComponent implements OnInit {

  @Input('ticket_class') ticket_class: FlightClass;

  display: boolean = false;

  constructor(private modalService: NgbModal) {
  }

  ngOnInit() {
  }

  openVerticallyCentered(content) {
    this.modalService.open(content, {centered: true});
  }
}
