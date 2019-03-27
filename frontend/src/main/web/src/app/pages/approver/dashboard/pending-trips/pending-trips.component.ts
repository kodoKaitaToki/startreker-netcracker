import {Component, OnInit} from '@angular/core';
import {ServiceTripPendingService} from "../shared/service/service-trip-pending.service";
import {PendingTrip} from "../shared/model/pending-trip.model";
import {HttpResponse} from '@angular/common/http';
import {checkToken} from 'src/app/modules/api';
import {clone} from 'ramda';

@Component({
  selector: 'app-pending-trips',
  templateUrl: './pending-trips.component.html',
  styleUrls: ['./pending-trips.component.scss'],
  providers: [ServiceTripPendingService]
})
export class PendingTripsComponent implements OnInit {

  private pendingTrips: PendingTrip[];

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

  totalRec: number;

  page: number = 1;

  entriesAmountOnPage = 3;

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
      .subscribe((resp: HttpResponse<any>) => {
        checkToken(resp.headers);
        this.pendingTrips = clone(resp.body);
      })
  }

  getFilterCriteria($event) {
    this.currentFilterCriteria = $event.value;
  }

  onChangePage($event) {

    this.page = $event;
    window.scrollTo(0, 0);
  }
}
