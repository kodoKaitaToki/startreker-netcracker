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

    public getAllServices(){
        return this.http.get<any>(Api.service.services(), HttpOptionsAuthorized);
    }

    public getPaginServices(from:Number, number: Number){
        let url = Api.service.paginServices() + '?from=' + from + '&number=' + number;
        return this.http.get<any>(url, HttpOptionsAuthorized);
    }

  public getServices(from: number, number: number, status: string){
        let url = Api.service.services();
        let params = new HttpParams()
            .set("from", from.toString())
            .set("number", number.toString())
            .set("status", status.toString());

        return this.http.get<any>(url, {
            headers: HttpOptionsAuthorized.headers, 
            params: params,
            observe: HttpOptionsAuthorized.observe
        });
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
