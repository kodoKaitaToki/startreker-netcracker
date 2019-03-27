import {Component, Input, OnInit, Output, ViewEncapsulation} from '@angular/core';
import {FlightClass} from "../../../shared/models/flight-class.model";
import {SearchService} from "../../../shared/services/search.service";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {FlightService} from "../../../shared/models/flight-service.model";
import {Router} from "@angular/router";
import {Trip} from "../../../../../shared/model/trip.model";
import {BookedTicket} from '../../../../user/shared/model/bought_ticket.model';


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
  @Input('trip') trip: Trip;

  @Output('total_price') total_price: number;

  services: FlightService[] = [];

  constructor(private searchSvc: SearchService,
              public activeModal: NgbActiveModal,
              private router: Router) { }

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

    let chosenServices: FlightService[] = [];
    this.services.forEach(service => {
      if (service.isAdded) {
        chosenServices.push(service);
      }
    });

    this.parseStoredContent(chosenServices);

    if (localStorage.getItem('at') !== null) {
      //redirect to buy page
      console.log("Authorized user");
      this.activeModal.close('Close click');
      this.router.navigate(['user/cart']);
    } else {
      console.log("Non authorized user");
      this.activeModal.close('Close click');

      this.router.navigate(["login"], {state: {message: 'Please, log in or sign up to proceed!'}});
    }
  }

  parseStoredContent(services: FlightService[]) {
    //add total price with discount
    let ticket = new BookedTicket(this.trip,
                                  this.ticket_class,
                                  services,
      this.amount);
    let boughtTickets = [];
    let stContent = JSON.parse(sessionStorage.getItem('boughtTickets'));
    if(stContent !== null){
      if(stContent.length > 0){
        for(ticket of stContent){
          boughtTickets.push(ticket);
        }
      }
    }
    boughtTickets.push(ticket);
    sessionStorage.setItem('boughtTickets', JSON.stringify(boughtTickets));
  }
}
