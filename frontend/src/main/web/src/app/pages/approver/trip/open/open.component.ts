import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { clone } from 'ramda';
import {MessageService} from 'primeng/components/common/messageservice';

import { TripService } from '../shared/service/trip.service'; 
import { Trip } from '../shared/model/trip.model';
import { HttpResponse } from '@angular/common/http';
import { checkToken } from '../../../../modules/api';

@Component({
  selector: 'app-open-trip',
  templateUrl: './open.component.html',
  styleUrls: ['./open.component.scss']
})
export class OpenTripComponent implements OnInit {

  trips: Trip[] = [];
  loadingTrip: Trip;

  status = "OPEN";
  tripsPerPage = 10;
  recordNumber: number = 0;

  constructor(private tripService: TripService,
              private messageService: MessageService) { }

  ngOnInit() {
    this.getTrips();
  }

  getTrips(){
    this.tripService.getTrips(this.status, 0, 10)
                      .subscribe((resp: HttpResponse<any>) => {
                          checkToken(resp.headers);
                          this.loadingTrip = null;
                          this.trips = clone(resp.body);
                        },
                        error => {
                          console.log(error);
                          this.loadingTrip = null;
                        }
                      );
  }

  onAssign(trip: Trip) {
    trip.trip_status = "ASSIGNED";

    this.loadingTrip = trip;

    this.tripService.updateTripStatus(trip)
                    .subscribe(
                      (resp: HttpResponse<any>) => {
                        checkToken(resp.headers);
                        this.showMessage(this.createMessage('success',
                                                            'The trip was assigned by you',
                                                            'You can find it in "Assigned to me"'));
                        this.getTrips();
                        this.loadingTrip = null;
                      }, 
                      error => {
                        this.loadingTrip = null;
                      }
                    );
  }

  showMessage(msgObj: any){
    this.messageService.add(msgObj);
  }

  createMessage(severity: string, summary: string, detail: string): any {
    return {
      severity: severity,
      summary: summary,
      detail: detail
    };
  }

  onChange(event: number){
    this.recordNumber = event;
    window.scrollTo(0, 0);
    this.getTrips();
  }
}
