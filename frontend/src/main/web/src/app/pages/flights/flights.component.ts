import {Component, OnInit} from '@angular/core';
import {Trip} from "./shared/models/trip.model";
import {SearchService} from "./shared/services/search.service";
import {FlightClass} from "./shared/models/flight-class.model";

@Component({
  selector: 'app-flights',
  templateUrl: './flights.component.html',
  styleUrls: ['./flights.component.scss'],
})
export class FlightsComponent implements OnInit {

  trips : Trip[] = [];

  constructor(private searchSvc: SearchService) {

    let trip: Trip = new Trip();
    trip.departure_date = "20-02-2020 12:20:00";
    trip.departure_spaceport_name = "SpaceX";
    trip.departure_planet_name = "EARTH";
    trip.arrival_date = "28-02-2020 22:28:00";
    trip.arrival_spaceport_name = "Blackhole";
    trip.arrival_planet_name = "MOON";
    trip.ticket_classes = [];

    let ticket_class = new FlightClass();
    ticket_class.class_id = 2;
    ticket_class.class_name = "Chad";
    ticket_class.remaining_seats = 10;
    ticket_class.ticket_price = 2000;
    trip.ticket_classes.push(ticket_class);

    this.trips.push(trip);
    this.trips.push(trip);

  }

  ngOnInit() {

  }

  getTripsByCriteria(event) {

  }


  //Don't trust IDEA let it be non-static
  getUrl() {
    return "url('assets/images/bg.jpg')";
  }

}
