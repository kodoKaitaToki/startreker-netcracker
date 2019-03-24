import { HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import { Injectable } from '@angular/core';

import { Api } from '../../../../../modules/api';
import { Carrier } from '../model/carrier.model';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class CarrierCrudService {

    constructor(private http: HttpClient) {
    }

    public getAllCarriers(){
        return this.http.get<any>(Api.carrier.carriers(), Api.HttpOptionsAuthorized);
    }

    public getCarriersPagin(from: number, to: number){
        let url = Api.carrier.getCarriersPagin() + '?from=' + from + '&number=' + to;
        return this.http.get<any>(url, Api.HttpOptionsAuthorized);
    }

    public getCarrierByUsername(name: String){
        let url = Api.carrier.getCarrierByUsername() + name;
        return this.http.get<Carrier>(url, Api.HttpOptionsAuthorized);
    }

    public addCarrier(carrier: Carrier){
        return this.http.post(Api.carrier.carriers(), carrier, Api.HttpOptionsAuthorized);
    }

    public deleteCarrier(id: number){
        let url = Api.carrier.carriers() + '/' + id;
        return this.http.delete(url, Api.HttpOptionsAuthorized);
    }

    public updateCarrier(carrier: Carrier, id: number){
        let updatedCarrier = new Object({username : carrier.username,
                                        email : carrier.email,
                                        telephone_number : carrier.telephone_number,
                                        is_activated : carrier.is_activated,
                                        id : id});
        return this.http.put(Api.carrier.carriers(), updatedCarrier, Api.HttpOptionsAuthorized);
    }

    public getCarrierNumber(): Observable<any>{
        return this.http.get(Api.carrier.getCarrierNumber());
    }
}
