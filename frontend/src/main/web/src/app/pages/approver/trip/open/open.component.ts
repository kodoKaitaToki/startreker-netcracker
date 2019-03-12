import { Component, OnInit } from '@angular/core';
import { clone } from 'ramda';

import { TripService } from '../trip.service'; 
import { Trip } from '../trip.model';

@Component({
  selector: 'app-open',
  templateUrl: './open.component.html',
  styleUrls: ['./open.component.scss']
})
export class OpenTripComponent implements OnInit {

  trips: Trip[] = [];

  constructor(private tripService: TripService) { }

  ngOnInit() {
    this.getTrips(3);
  }

  getTrips(status: Number){
    this.tripService.getTrips(status, 1, 10)
                      .subscribe(
                        (resp: Response) => {
                          /*if (resp.headers.get('New-Access-Token')) {
                            localStorage.removeItem('at');
                            localStorage.setItem('at', resp.headers.get('New-Access-Token'));
                          }*/
                          this.trips = clone(resp);
                        },
                        error => console.log(error)
                      );
  }

}
