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
        return this.http.get<any>(Api.carrier.carriers(), HttpOptionsAuthorized);
    }

    public getCarriersPagin(from: number, to: number){
        let url = Api.carrier.getCarriersPagin() + '?from=' + from + '&number=' + to;
        return this.http.get<any>(url, HttpOptionsAuthorized);
    }

    public getCarrierByUsername(name: String){
        let url = Api.carrier.getCarrierByUsername() + name;
        return this.http.get<Carrier>(url, HttpOptionsAuthorized);
    }

    public addCarrier(carrier: Carrier){
        return this.http.post(Api.carrier.carriers(), carrier, HttpOptionsAuthorized);
    }

    public deleteCarrier(id: number){
        let url = Api.carrier.carriers() + '/' + id;
        return this.http.delete(url, HttpOptionsAuthorized);
    }

    public updateCarrier(carrier: Carrier){
        return this.http.put(Api.carrier.carriers(), carrier, HttpOptionsAuthorized);
    }
}
