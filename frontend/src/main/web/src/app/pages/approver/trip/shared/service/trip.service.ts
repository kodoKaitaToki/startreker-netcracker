import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Api, HttpOptionsAuthorized } from '../../../../../modules/api';
import { Trip } from '../model/trip.model';

@Injectable({
    providedIn: 'root'
})
export class TripService{

    constructor(private http: HttpClient){}

    public getTrips(status: String, offset: Number, limit: Number){
        let uri = Api.trip.trips() + '?status=' + status + '&offset=' + offset + '&limit=' + limit;
        return this.http.get(uri, HttpOptionsAuthorized);
    }

    public updateTripStatus(trip: Trip){
        let uri = Api.trip.update() + '/' + trip.trip_id;
        let updatedTrip = {
            trip_status: trip.trip_status
        };
        return this.http.patch(uri, updatedTrip, HttpOptionsAuthorized);
    }

    public updateTripReply(trip: Trip){
        let uri = Api.trip.update() + '/' + trip.trip_id;
        let updatedTrip = {
            trip_status: trip.trip_status,
            trip_reply: trip.trip_reply
        };
        console.log(updatedTrip);
        return this.http.patch(uri, updatedTrip,HttpOptionsAuthorized);
    }
}