import {Component, OnDestroy, OnInit} from '@angular/core';
import {Trip} from "../../shared/model/trip.model";
import {SearchService} from "./shared/services/search.service";
import {FlightClass} from "./shared/models/flight-class.model";
import {DatePipe} from "@angular/common";
import {Subscription} from "rxjs/internal/Subscription";

@Component({
  selector: 'app-flights',
  templateUrl: './flights.component.html',
  styleUrls: ['./flights.component.scss'],
})
export class FlightsComponent implements OnInit, OnDestroy {

  trips: Trip[] = [];

  private sub: Subscription;

  constructor(private searchSvc: SearchService,
              datePipe: DatePipe) {
    // let trip: Trip = new Trip();
    // trip.departure_date = "20-02-2020 12:20";
    // trip.departure_spaceport_name = "SpaceX";
    // trip.departure_planet_name = "EARTH";
    // trip.arrival_date = "28-02-2020 22:30";
    // trip.arrival_spaceport_name = "Blackhole";
    // trip.arrival_planet_name = "MOON";
    // trip.ticket_classes = [];

    // let ticket_class = new FlightClass();
    // ticket_class.class_id = 2;
    // ticket_class.class_name = "Chad";
    // ticket_class.remaining_seats = 10;
    // ticket_class.ticket_price = 2000;
    // trip.ticket_classes.push(ticket_class);

    // this.trips.push(trip);
    // this.trips.push(trip);

  }

  ngOnInit() {}

  getTripsByCriteria(event) {
    this.searchSvc.getTrips(
      event.startPlanet,
      event.startSpaceport,
      event.startDate,
      event.finishPlanet,
      event.finishSpaceport
    ).subscribe((response) => {
      this.trips = response;
    })
  }

  getUrl() {
    return "url('assets/images/bg.jpg')";
  }

  ngOnDestroy() {this.sub.unsubscribe();}

}
