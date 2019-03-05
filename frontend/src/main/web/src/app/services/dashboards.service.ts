import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { clone } from 'ramda';

import { Api, HttpOptionsAuthorized } from '../modules/api';

import { ServiceList } from './interfaces/service-dashboard.interface';
import { TripList } from './interfaces/trip-dashboard.interface';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})

export class ApiDashboardService {
  tripData: TripList;
  serviceData: ServiceList;

  constructor(
    private http: HttpClient,
    private router: Router
    ) { }

    get getTrip() {
        return this.tripData;
    }


    get getService() {
        return this.serviceData;
    }

    setTripDistribution(): Observable<any> {
        return this.http.get<any>(Api.dashboard.tripDistribution(), HttpOptionsAuthorized);
    }

    setServiceDistribution(): Observable<any> {
        return this.http.get<any>(Api.dashboard.serviceDistribution(), HttpOptionsAuthorized);
    }
}
