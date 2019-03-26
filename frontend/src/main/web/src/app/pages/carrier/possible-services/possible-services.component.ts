import { Component, OnInit } from '@angular/core';
import { Trip } from '../trips/shared/model/trip';
import { TripsService } from '../../../services/trips.service';
import { TripTransfer } from '../trip-transfer';

@Component({
  selector: 'app-possible-services',
  templateUrl: './possible-services.component.html',
  styleUrls: ['./possible-services.component.scss']
})
export class PossibleServicesComponent implements OnInit {

  trip: Trip;

  constructor(
    private tripTransfer: TripTransfer) { 
      
    }

  ngOnInit() {
    this.getTrips();
  }

  getTrips() {
    this.trip = this.tripTransfer.storage;
  }

}
