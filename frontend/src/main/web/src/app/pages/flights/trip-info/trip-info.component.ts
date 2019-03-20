import {Component, Input, OnInit} from '@angular/core';
import {Trip} from "../shared/models/trip.model";

@Component({
  selector: 'app-trip-info',
  templateUrl: './trip-info.component.html',
  styleUrls: ['./trip-info.component.scss']
})
export class TripInfoComponent implements OnInit {

  @Input("trip") trip: Trip;

  constructor() { }

  ngOnInit() {
  }

}
