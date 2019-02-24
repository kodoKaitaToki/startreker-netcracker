import { HttpClient, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { Configuration } from '../../../../app.constants';

@Injectable()
export class DashCostService {

    private actionUrl: string;

    constructor(private http: HttpClient, private _configuration: Configuration) {
        this.actionUrl = _configuration.ServerWithApiUrl + 'costs';
    }

    public getCarrier<T>(id: number): Observable<T>{
        return this.http.get<T>(this.actionUrl + '?' + 'carrier_id=' + id);
    }

    public getCarrierPerWeak<T>(id: number): Observable<T>{
        return this.http.get<T>(this.actionUrl + '/per-week/?' + 'carrier_id=' + id);
    }

    public getCarrierPerMonth<T>(id: number): Observable<T>{
        return this.http.get<T>(this.actionUrl + '/per-month/?' + 'carrier_id=' + id);
    }

    public getCarrierPerInterval<T>(id: number, from: String, to: String): Observable<T>{
        return this.http.get<T>(this.actionUrl + '/per-period/?' + 'carrier_id=' + id + "&from=" + from + "&to=" + to);
    }

}


@Injectable()
export class CustomInterceptor implements HttpInterceptor {

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (!req.headers.has('Content-Type')) {
            req = req.clone({ headers: req.headers.set('Content-Type', 'application/json') });
        }

        req = req.clone({ headers: req.headers.set('Accept', 'application/json') });
        console.log(JSON.stringify(req.headers));
        return next.handle(req);
    }
}
