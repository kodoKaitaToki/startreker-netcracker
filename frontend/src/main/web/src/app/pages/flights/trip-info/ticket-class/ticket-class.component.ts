import {Component, Input, OnInit} from '@angular/core';
import {FlightClass} from "../../shared/models/flight-class.model";

@Component({
  selector: 'app-ticket-class',
  templateUrl: './ticket-class.component.html',
  styleUrls: ['./ticket-class.component.scss']
})
export class TicketClassComponent implements OnInit {

  @Input('ticket_class') ticket_class: FlightClass;

  constructor() { }

  ngOnInit() {
  }

}
