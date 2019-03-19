import {Component, OnInit} from '@angular/core';
import {Trip} from "./shared/models/trip.model";
import {SearchService} from "./shared/services/search.service";
import {FormControl, FormGroup} from "@angular/forms";
import {FlightClass} from "./shared/models/flight-class.model";

@Component({
  selector: 'app-flights',
  templateUrl: './flights.component.html',
  styleUrls: ['./flights.component.scss']
})
export class FlightsComponent implements OnInit {

  trips : Trip[] = [];

  searchForm: FormGroup;

  constructor(private svc: SearchService) {
    //this.getTripsByCriteria()

    let trip: Trip = new Trip();
    trip.departure_date = "20-02-2020 12:20:00";
    trip.departure_spaceport_name = "SpaceX";
    trip.departure_planet_name = "EARTH";
    trip.arrival_date = "28-02-2020 22:28:00"
    trip.arrival_spaceport_name = "Blackhole";
    trip.arrival_planet_name = "MOON";
    trip.ticket_classes = [];

    let ticket_class = new FlightClass();
    ticket_class.class_name = "Chad";
    ticket_class.remaining_seats = 10;
    ticket_class.ticket_price = 2000;
    trip.ticket_classes.push(ticket_class);

    this.trips.push(trip);
    this.trips.push(trip);
  }

  ngOnInit() {
    this.searchForm = new FormGroup({
      startPlanet: new FormControl(''),
      finishPlanet: new FormControl(''),
      startSpaceport: new FormControl(''),
      finishSpaceport: new FormControl(''),
      startDate: new FormControl(''),
      finishDate: new FormControl('')
    });

    this.searchForm = JSON.parse(localStorage.getItem('formData'));
  }

  getTripsByCriteria() {
    this.svc.getTrips("", "", "", "", "").subscribe((response) => {
      this.trips = response;
    })
  }


  getUrl() {
    return "url('assets/images/bg.jpg')";
  }

}
