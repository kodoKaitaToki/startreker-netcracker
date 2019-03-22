import {Component, Input, OnInit} from '@angular/core';
import {FlightClass} from "../../../shared/models/flight-class.model";
import {SearchService} from "../../../shared/services/search.service";
import {PossibleService} from "../../../../carrier/suggestions/shared/model/possible-service";


@Component({
  selector: 'app-ticket-class-info',
  templateUrl: './ticket-class-info.component.html',
  styleUrls: ['./ticket-class-info.component.scss']
})
export class TicketClassInfoComponent implements OnInit {

  @Input('ticket_class') ticket_class: FlightClass;

  services: PossibleService[] = [];

  constructor(private searchSvc: SearchService,
  ) {
  }

  ngOnInit() {
    this.searchSvc.getServices(this.ticket_class.class_id).subscribe(resp =>
      this.services = resp);
  }


}
