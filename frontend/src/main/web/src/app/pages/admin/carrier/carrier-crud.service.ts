import { HttpClient} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { Api } from '../../../modules/api/index';
import { Carrier } from './carrier';

@Injectable({
    providedIn: 'root'
})
export class CarrierCrudService {

    constructor(private http: HttpClient) {
    }

    public getAllCarriers(){
        return this.http.get<any>(Api.carrier.carriers());
    }

    public getCarriersPagin<Carrier>(from: number, to: number): Observable<Carrier>{
        let url = Api.carrier.getCarriersPagin() + '?from=' + from + '&number=' + to;
        return this.http.get<any>(url);
    }

    public getCarrierByUsername<Carrier>(name: String): Observable<Carrier>{
        let url = Api.carrier.getCarrierByUsername() + name;
        return this.http.get<Carrier>(url);
    }

    public addCarrier<T>(carrier: Carrier): Observable<T>{
        return this.http.post<T>(Api.carrier.carriers(), carrier);
    }

    public deleteCarrier<T>(id: Number): Observable<T>{
        let url = Api.carrier.carriers() + '/' + id;
        return this.http.delete<T>(url);
    }

    public updateCarrier<T>(carrier: Carrier): Observable<T>{
        return this.http.put<T>(Api.carrier.carriers(), carrier);
    }
}
