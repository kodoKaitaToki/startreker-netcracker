import { HttpClient, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { Api } from '../../../modules/api/index';
import { HttpOptionsAuthorized } from '../../../modules/api/index';

@Injectable()
export class DashCostService {

    private actionUrl: string;

    constructor(private http: HttpClient) {
    }

    public getCosts<T>(from: string, to: string): Observable<T>{
        let url = Api.costDash.getCosts() + '?' + 'from=' + from + '&to=' + to;
        return this.http.get<any>(url);
    }

    public getCarCosts<T>(id: number, from: string, to: string): Observable<T>{
        let url = Api.costDash.getCosts() + '/' + id + '?' + 'from=' + from + '&to=' + to;
        return this.http.get<T>(url);
    }
}
