import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { clone } from 'ramda';

import { Api, HttpOptionsAuthorized } from '../modules/api';

import { ServiceList } from './interfaces/service-dashboard.interface';
import { TripList } from './interfaces/trip-dashboard.interface';

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

    setTripDistribution() {
        this.http.get<any>(Api.dashboard.tripDistribution(), HttpOptionsAuthorized)
            .subscribe(
                (resp: Response) => {
                    if (resp.headers.get('New-Access-Token')) {
                        localStorage.removeItem('at');
                        localStorage.setItem('at', resp.headers.get('New-Access-Token'));
                    }
                    this.tripData = clone(resp.body);
                },
                error => console.log(error)
            );
    }

    setServiceDistribution() {
        this.http.get<any>(Api.dashboard.serviceDistribution(), HttpOptionsAuthorized)
        .subscribe(
            (resp: Response) => {
                if (resp.headers.get('New-Access-Token')) {
                    localStorage.removeItem('at');
                    localStorage.setItem('at', resp.headers.get('New-Access-Token'));
                }
                this.serviceData = clone(resp.body);
            },
            error => console.log(error)
        );
    }
}