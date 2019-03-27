import { Component, OnInit } from '@angular/core';
import { clone } from 'ramda';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {MessageService} from 'primeng/components/common/messageservice';

import { Trip } from '../shared/model/trip.model';
import { TripService } from '../shared/service/trip.service'; 
import { checkToken } from '../../../../modules/api';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'app-assigned-trip',
  templateUrl: './assigned.component.html',
  styleUrls: ['./assigned.component.scss']
})
export class AssignedTripComponent implements OnInit {

  trips: Trip[] = [];
  loadingTrip: Trip;

  form: FormGroup;

  currentTripForReply: Trip;

  status = "ASSIGNED";
  tripsPerPage = 10;
  recordNumber: number = 0;

  constructor(private tripService: TripService,
              private messageService: MessageService) { }

  ngOnInit() {
    this.getTrips();
    this.setFormInDefault();
  }

  setFormInDefault() {
    this.form = new FormGroup(
      {
        reply_text: new FormControl('', [Validators.required, Validators.minLength(3)]),
      }
    );
  }
  
  resetReply() {
    this.currentTripForReply = null;
    this.form.reset();
  }

  getTrips(){
    this.tripService.getTrips(this.status, this.recordNumber, this.tripsPerPage)
                      .subscribe((resp: HttpResponse<any>) => {
                          checkToken(resp.headers);
                          this.loadingTrip = null;
                          this.trips = clone(resp.body);
                        }
                      );
  }

  onReview(trip: Trip) {
    trip.trip_status = "UNDER_CLARIFICATION";

    trip.trip_reply.push({
      trip_id: trip.trip_id,
      reply_text: this.form.get('reply_text').value,
      writer_id: 0,
      writer_name: "",
      creation_date: new Date()
    });

    this.loadingTrip = trip;

    this.tripService.updateTripReply(trip)
                    .subscribe(
                      (resp: HttpResponse<any>) => {
                          checkToken(resp.headers);
                          this.showMessage(this.createMessage('success',
                                                              'The trip is succeesfully reviewed',
                                                              'The carrier of the trip will see your reply'));
                          this.getTrips();
                      }, 
                      error => {
                        this.loadingTrip = null;
                      }
                    );
  }

  onPublish(trip: Trip){

    trip.trip_status = "PUBLISHED";
    this.loadingTrip = trip;

    this.tripService.updateTripStatus(trip)
                    .subscribe(
                      (resp: HttpResponse<any>) => {
                        checkToken(resp.headers);
                        this.showMessage(this.createMessage('success',
                                                            'The trip was published',
                                                            'Users can buy tickets of this trip'));
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
