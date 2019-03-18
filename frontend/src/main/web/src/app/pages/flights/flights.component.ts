import { Component, OnInit } from '@angular/core';
import {Trip} from "./shared/models/trip.model";
import {SearchService} from "./shared/services/search.service";

@Component({
  selector: 'app-flights',
  templateUrl: './flights.component.html',
  styleUrls: ['./flights.component.scss']
})
export class FlightsComponent implements OnInit {

  trips : Trip[] = [];

  constructor(private svc: SearchService) {

  }

  ngOnInit() {

  }

  getTripsByCriteria() {

  }


  getUrl() {
    return "url('assets/images/bg.jpg')";
  }

}
