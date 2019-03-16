import { HttpClient, HttpHeaders} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { Api } from '../../../modules/api/index';
import { HttpOptionsAuthorized } from '../../../modules/api/index';
import { Carrier } from './carrier';

@Injectable({
    providedIn: 'root'
})
export class CarrierCrudService {

    constructor(private http: HttpClient) {
    }

    public getAllCarriers(){
        let headers = new HttpHeaders({
            //'Content-Type':  'application/json',
    //'Access-Control-Allow-Origin': '*',
          'Authorization': `Bearer ${localStorage.getItem('at')}`,
          'Authorization-Refresh': `Bearer ${localStorage.getItem('rt')}`,
          //'Access-Control-Allow-Methods': '*'
          //'Access-Control-Allow-Methods': 'POST, GET, PUT, OPTIONS, DELETE',
          //'Access-Control-Allow-Headers': '*',
          //  'Access-Control-Allow-Headers': 'Origin, X-Requested-With, Content-Type, '
          //                                  + 'Accept, X-Auth-Token, X-Csrf-Token, Authorization, Authorization-Refresh',
          // 'Access-Control-Allow-Credentials': 'false',
          // 'Access-Control-Max-Age': '3600'

            'Content-Type':  'application/json',
            'Access-Control-Allow-Origin': '*'
        });
    // let headers = new HttpHeaders();
    // headers = headers.append('Content-Type', 'application/json');
      //TODO: remove debug backdoor/
    // headers = headers.append('Authorization', 'debug_login 7');
    console.log(headers);
        return this.http.get<any>(Api.carrier.carriers(), {headers: headers});
    }

    public getCarriersPagin<Carrier>(from: number, to: number): Observable<Carrier>{
        let url = Api.carrier.getCarriersPagin() + '?from=' + from + '&number=' + to;
        return this.http.get<any>(url, HttpOptionsAuthorized);
    }

    public getCarrierByUsername<Carrier>(name: String): Observable<Carrier>{
        let url = Api.carrier.getCarrierByUsername() + name;
        return this.http.get<Carrier>(url, HttpOptionsAuthorized);
    }

    public addCarrier<T>(carrier: Carrier): Observable<T>{
        return this.http.post<T>(Api.carrier.carriers(), carrier, HttpOptionsAuthorized);
    }

    public deleteCarrier<T>(id: number): Observable<T>{
        let url = Api.carrier.carriers() + '/' + id;
        return this.http.delete<T>(url, HttpOptionsAuthorized);
    }

    public updateCarrier<T>(carrier: Carrier): Observable<T>{
        return this.http.put<T>(Api.carrier.carriers(), carrier, HttpOptionsAuthorized);
    }
}
