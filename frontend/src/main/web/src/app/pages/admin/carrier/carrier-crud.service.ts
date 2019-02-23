import { HttpClient, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { Api } from '../../../modules/api/index';
import { HttpOptionsAuthorized } from '../../../modules/api/index';
import { AddCarrier } from './addCarrier';

@Injectable({
    providedIn: 'root'
})
export class CarrierCrudService {

    allCarrier;

    constructor(private http: HttpClient) {
    }

    getCarriers(){
        return this.allCarrier;
    }

    public getAllCarriers(){
        return this.http.get<any>(Api.carrier.carriers());
    }

    public getCarriersPagin<Carrier>(from: number, to: number): Observable<Carrier>{
        let url = Api.carrier.getCarriersPagin() + '?from=' + from + '&number=' + to;
        return this.http.get<any>(url);
    }

    /*public getCarrierByName<Carrier>(name: String): Observable<Carrier>{
        let url = this.actionUrl + '/carrier-by-username?username=' + name;
        return this.http.get<Carrier>(url, HttpOptionsAuthorized);
    } 
    
    public getCarrierByEmail<Carrier>(email: String): Observable<Carrier>{
        let url = this.actionUrl + '/carrier-by-email?email=' + email;
        return this.http.get<Carrier>(url, HttpOptionsAuthorized);
    }  

    public getCarrierById<Carrier>(id: number): Observable<Carrier>{
        let url = this.actionUrl + '/carrier/' + id;
        return this.http.get<Carrier>(url, HttpOptionsAuthorized);
    } */

    public addCarrier<T>(carrier: AddCarrier): Observable<T>{
        return this.http.post<T>(Api.carrier.carriers(), carrier);
    }

    public deleteCarrier<T>(id: Number): Observable<T>{
        let url = Api.carrier.carriers() + '/' + id;
        return this.http.delete<T>(url);
    }

    public updateCarrier<Carrier>(carrier: Carrier): Observable<Carrier>{
        return this.http.put<Carrier>(Api.carrier.carriers(), carrier);
    }
}
