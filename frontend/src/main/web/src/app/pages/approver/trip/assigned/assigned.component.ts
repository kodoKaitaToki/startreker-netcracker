import { Component, OnInit } from '@angular/core';
import { clone } from 'ramda';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {MessageService} from 'primeng/components/common/messageservice';

import { Trip } from '../shared/model/trip.model';
import { TripService } from '../shared/service/trip.service'; 

@Component({
  selector: 'app-assigned',
  templateUrl: './assigned.component.html',
  styleUrls: ['./assigned.component.scss']
})
export class AssignedTripComponent implements OnInit {

  trips: Trip[] = [];
  loadingTrip: Trip;

  moreFlag: Boolean = false;

  form: FormGroup;

  currentTripForReply: Trip;

  pageNumber: number = 10;

  pageFrom: number;

  constructor(private tripService: TripService,
              private messageService: MessageService) { }

  ngOnInit() {
    this.pageFrom = 0;
    this.getTrips("ASSIGNED");
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

  getTrips(status: String){
    this.tripService.getTrips(status, 0, 10)
                      .subscribe(
                        (resp: Response) => {
                          /*if (resp.headers.get('New-Access-Token')) {
                            localStorage.removeItem('at');
                            localStorage.setItem('at', resp.headers.get('New-Access-Token'));
                          }*/
                          this.loadingTrip = null;
                          this.trips = clone(resp);
                        }
                      );
  }

  onReview(trip: Trip) {
    this.moreFlag = false;
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
                      (resp: Response) => {
                        /*if (resp.headers.get('New-Access-Token')) {
                            localStorage.removeItem('at');
                            localStorage.setItem('at', resp.headers.get('New-Access-Token'));
                          }*/
                          this.showMessage(this.createMessage('success',
                                                              'The trip is succeesfully reviewed',
                                                              'The carrier of the trip will see your reply'));
                          this.getTrips("ASSIGNED");
                      }, 
                      error => {
                        this.loadingTrip = null;
                      }
                    );
  }

  onPublish(trip: Trip){
    this.moreFlag = false;
    trip.trip_status = "PUBLISHED";
    this.loadingTrip = trip;

    this.tripService.updateTripStatus(trip)
                    .subscribe(
                      (resp: Response) => {
                        /*if (resp.headers.get('New-Access-Token')) {
                            localStorage.removeItem('at');
                            localStorage.setItem('at', resp.headers.get('New-Access-Token'));
                          }*/
                        this.showMessage(this.createMessage('success',
                                                            'The trip was published',
                                                            'Users can buy tickets of this trip'));
                        this.getTrips("ASSIGNED");
                        this.loadingTrip = null;
                      }, 
                      error => {
                        this.loadingTrip = null;
                      }
                    );
  }

  onPageUpdate(from: number) {
    this.pageFrom = from;
    this.getTrips("ASSIGNED");
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
