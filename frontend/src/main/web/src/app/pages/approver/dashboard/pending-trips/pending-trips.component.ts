import {Component, OnInit} from '@angular/core';
import {ServiceTripPendingService} from "../shared/service/service-trip-pending.service";
import {PendingTrip} from "../shared/model/pending-trip.model";

@Component({
  selector: 'app-pending-trips',
  templateUrl: './pending-trips.component.html',
  styleUrls: ['./pending-trips.component.scss'],
  providers: [ServiceTripPendingService]
})
export class PendingTripsComponent implements OnInit {

  private pendingTrips: PendingTrip[];

  private currentPendingTrip: PendingTrip = null;

  private filterCriterias = [
    'carrierName',
    'approverName',
    'departureDate',
    'arrivalDate',
    'departmentPlanetName',
    'arrivalPlanetName',
    'departmentSpaceportName',
    'arrivalSpaceportName',
    'tripStatus'
  ];

  private currentFilterCriteria: string;

  private currentFilterContent: string;

  constructor(private pendingSrvc: ServiceTripPendingService) {
  }

  ngOnInit() {
    this.currentFilterCriteria = this.filterCriterias[0];
    this.getAllPendingTrips();
  }

  public getAllPendingTrips() {
    this.pendingSrvc.getAllPendingTrips()
      .subscribe((data) => {
        this.pendingTrips = data;
      })
  }

  onDetailClick(pendingTrip) {
    this.currentPendingTrip = pendingTrip;
  }

  onCloseDetailClick() {
    this.currentPendingTrip = null;
  }

  getFilterCriteria($event) {
    this.currentFilterCriteria = $event.value;
  }
}
