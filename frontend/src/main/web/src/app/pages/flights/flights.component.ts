import {Component, OnInit} from '@angular/core';
import {Trip} from "./shared/models/trip.model";
import {SearchService} from "./shared/services/search.service";
import {FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-flights',
  templateUrl: './flights.component.html',
  styleUrls: ['./flights.component.scss']
})
export class FlightsComponent implements OnInit {

  trips : Trip[] = [];

  searchForm: FormGroup;

  constructor(private svc: SearchService) {
    this.getTripsByCriteria()
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
