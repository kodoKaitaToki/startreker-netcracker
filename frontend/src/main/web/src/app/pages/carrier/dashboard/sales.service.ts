import { HttpClient, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpHeaders, HttpParams} from '@angular/common/http';
import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import { map } from 'rxjs/operators';

import { SalesModel } from './sales/sales.model';

import { Api } from '../../../modules/api/index';

@Injectable({
  providedIn: 'root'
})
export class SalesService {
  private actionUrl: string;

  constructor(private http: HttpClient) {
    this.actionUrl = Api.baseUrl + 'api/v1';
  }

  public getServicesSalesStatisticsByWeek(from: string, to: string): Observable<SalesModel[]>{
    return this.getSalesStatistics(from, to, "service/sales/per_week");
  }
  public getServicesSalesStatisticsByMonth(from: string, to: string): Observable<SalesModel[]>{
    return this.getSalesStatistics(from, to, "service/sales/per_month");
  }

  public getTripsSalesStatisticsByWeek(from: string, to: string): Observable<SalesModel[]>{
    return this.getSalesStatistics(from, to, "trip/sales/per_week");
  }
  public getTripsSalesStatisticsByMonth(from: string, to: string): Observable<SalesModel[]>{
    return this.getSalesStatistics(from, to, "trip/sales/per_month");
  }

  public getSalesStatistics(from: string, to: string, type: string): Observable<SalesModel[]>{
      let headers = new HttpHeaders();
      headers = headers.append('Content-Type', 'application/json');
      //TODO: remove debug backdoor
      headers = headers.append('Authorization', 'debug_login 21');

      let params = new HttpParams().set("from", from).set("to", to);

      return this.http.get<any>(this.actionUrl + "/" + type, {headers: headers, params: params})
        .pipe(map(res => {

          return res.map(item => {
            return new SalesModel(
                item.sold,
                item.revenue,
                new Date(item.from),
                new Date(item.to),
            );
          });
      }))
  }

}
