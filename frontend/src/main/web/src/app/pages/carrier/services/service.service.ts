import { HttpClient} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Api } from '../../../modules/api/index';

import { Service } from './service.model';

@Injectable({
    providedIn: 'root'
})
export class ServiceService{

    constructor(private http: HttpClient) {
    }

    public getAllServices(){
        return this.http.get<any>(Api.service.services());
    }

    public getPaginServices<Service>(from:Number, number: Number): Observable<Service>{
        let url = Api.service.paginServices() + '?from=' + from + '&number=' + number;
        return this.http.get<any>(url);
    }

    public getServiceByStatus<Service>(status: Number): Observable<Service>{
        let url = Api.service.services() + '?' + status;
        return this.http.get<any>(url);
    }

    public addService<T>(service: Service): Observable<T>{
        return this.http.post<T>(Api.service.services(), service);
    }

    public deleteCarrier<T>(id: Number): Observable<T>{
        let url = Api.carrier.carriers() + '/' + id;
        return this.http.delete<T>(url);
    }

    public updateCarrier<T>(service: Service): Observable<T>{
        return this.http.put<T>(Api.service.services(), service);
    }
}