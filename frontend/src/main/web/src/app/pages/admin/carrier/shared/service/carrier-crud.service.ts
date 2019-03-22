import { HttpClient, HttpHeaders} from '@angular/common/http';
import { Injectable } from '@angular/core';

import { Api } from '../../../../../modules/api';
import { Carrier } from '../model/carrier.model';

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

    public updateCarrier(carrier: Carrier){
        return this.http.put(Api.carrier.carriers(), carrier, Api.HttpOptionsAuthorized);
    }
}
