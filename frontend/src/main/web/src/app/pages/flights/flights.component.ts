import {Component, OnDestroy, OnInit} from '@angular/core';
import {Trip} from "../../shared/model/trip.model";
import {SearchService} from "./shared/services/search.service";
import {Subscription} from "rxjs/internal/Subscription";

@Component({
  selector: 'app-flights',
  templateUrl: './flights.component.html',
  styleUrls: ['./flights.component.scss'],
})
export class FlightsComponent implements OnInit, OnDestroy {

  trips: Trip[] = [];

  private sub: Subscription;

  constructor(private searchSvc: SearchService) {
  }

  ngOnInit() {
  }

  getTripsByCriteria(event) {
    console.log(JSON.stringify(event));
    this.sub = this.searchSvc.getTrips(
      event.startPlanet,
      event.finishPlanet,
      event.startDate,
      event.startSpaceport,
      event.finishSpaceport
    ).subscribe((response) => {
      this.trips = response;
    });
  }

  getUrl() {
    return "url('assets/images/bg.jpg')";
  }

  ngOnDestroy() {
    if (this.sub !== undefined)
      this.sub.unsubscribe();
  }
}
