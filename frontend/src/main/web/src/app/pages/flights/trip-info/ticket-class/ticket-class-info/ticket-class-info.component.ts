import {Component, Input, OnInit, Output, ViewEncapsulation} from '@angular/core';
import {FlightClass} from "../../../shared/models/flight-class.model";
import {SearchService} from "../../../shared/services/search.service";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {FlightService} from "../../../shared/models/flight-service.model";
import {Router} from "@angular/router";


@Component({
  selector: 'app-ticket-class-info',
  templateUrl: './ticket-class-info.component.html',
  styleUrls: ['./ticket-class-info.component.scss'],
  //Used to override styles of modal window
  encapsulation: ViewEncapsulation.None
})
export class TicketClassInfoComponent implements OnInit {

  @Input('ticket_class') ticket_class: FlightClass;
  @Input('amount') amount: number;

  @Output('total_price') total_price: number;

  services: FlightService[] = [];

  constructor(private searchSvc: SearchService,
              public activeModal: NgbActiveModal,
              private router: Router
  ) {
  }

  ngOnInit() {
    this.searchSvc.getServices(this.ticket_class.class_id).subscribe(resp =>
      this.services = resp);
    this.total_price = this.ticket_class.ticket_price;
  }

  addService(service) {
    service.isAdded = true;
    this.total_price += service.service_price;
  }

  removeService(service) {
    service.isAdded = false;
    this.total_price -= service.service_price;
  }

  onBuyClick() {
    sessionStorage.setItem('amount', this.amount.toString());
    sessionStorage.setItem('ticket_class', JSON.stringify(this.ticket_class));

    let chosenServices: FlightService[] = [];
    this.services.forEach(service => {
      if (service.isAdded) {
        chosenServices.push(service);
      }
    });

    console.log(JSON.stringify(chosenServices));
    sessionStorage.setItem('services', JSON.stringify(chosenServices));

    if (localStorage.getItem('at') !== null) {
      //redirect to buy page
      console.log("Authorized user")
    } else {
      console.log("Non authorized user");
      this.activeModal.close('Close click');
      this.router.navigate(["login"]);
    }
  }
}
