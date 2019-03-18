import { Component, OnInit } from '@angular/core';
import { clone } from 'ramda';
import {MessageService} from 'primeng/components/common/messageservice';

import { TripService } from '../shared/service/trip.service'; 
import { Trip } from '../shared/model/trip.model';
import { HttpResponse } from '@angular/common/http';
import { checkToken } from '../../../../modules/api';

@Component({
  selector: 'app-open',
  templateUrl: './open.component.html',
  styleUrls: ['./open.component.scss']
})
export class OpenTripComponent implements OnInit {

  trips: Trip[] = [];
  loadingTrip: Trip;

  constructor(private tripService: TripService,
              private messageService: MessageService) { }

  ngOnInit() {
    this.getTrips("OPEN");
  }

  getTrips(status: String){
    this.tripService.getTrips(status, 0, 10)
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
    //trip.trip_reply.writer_id = 3;

    this.loadingTrip = trip;

    this.tripService.updateTripStatus(trip)
                    .subscribe(
                      (resp: HttpResponse<any>) => {
                        checkToken(resp.headers);
                        this.showMessage(this.createMessage('success',
                                                            'The trip was assigned by you',
                                                            'You can find it in "Assigned to me"'));
                        this.getTrips("ASSIGNED");
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

}
