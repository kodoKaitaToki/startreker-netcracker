import {HttpClient, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {Api, HttpOptionsAuthorized} from '../../../../../modules/api';

import {Service} from '../model/service.model';

@Injectable({
    providedIn: 'root'
})
export class ServiceService{

    constructor(private http: HttpClient) {
    }

    // public getAllServices(){
    //     return this.http.get<any>(Api.service.services(), HttpOptionsAuthorized);
    // }

    // public getPaginServices(from:Number, number: Number){
    //     let url = Api.service.paginServices() + '?from=' + from + '&number=' + number;
    //     return this.http.get<any>(url, HttpOptionsAuthorized);
    // }

    public getServiceByStatus(status: string, from: number, amount: number){
        let params = new HttpParams().set('status', status)
                                    .set('from', from.toString())
                                    .set('number', amount.toString());
        return this.http.get<any>(Api.service.services(), {params: params,
                                                            headers: HttpOptionsAuthorized.headers,
                                                           observe: HttpOptionsAuthorized.observe});
    }

    public getServicesAmount(status: string){
        let url = Api.service.servicesAmount() + '?status=' + status;
        return this.http.get(url, Api.HttpOptionsAuthorized);
    }

    public addService(service: Service){
        return this.http.post<any>(Api.service.services(), service, HttpOptionsAuthorized);
    }

    public deleteService(id: Number){
        let url = Api.carrier.carriers() + '/' + id;
        return this.http.delete(url, HttpOptionsAuthorized);
    }

    public updateService(service: Service){
        return this.http.put<any>(Api.service.services(), service, HttpOptionsAuthorized);
    }
}
