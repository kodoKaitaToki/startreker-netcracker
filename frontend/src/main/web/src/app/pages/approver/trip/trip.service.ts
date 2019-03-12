import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders} from '@angular/common/http';

import { Api } from '../../../modules/api';

@Injectable({
    providedIn: 'root'
})
export class TripService{

    constructor(private http: HttpClient){}

    public getTrips(status: Number, offset: Number, limit: Number){
        let headers = new HttpHeaders();
        headers = headers.append('Content-Type', 'application/json');
        headers = headers.append('Authorization', 'debug_login 3');
        let uri = Api.trip.trips() + '?status=' + status + '&offset=' + offset + '&limit=' + limit;
        return this.http.get<any>(uri, {headers});
    }
}