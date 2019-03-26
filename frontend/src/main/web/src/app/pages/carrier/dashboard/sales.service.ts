import { HttpClient, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpHeaders, HttpParams} from '@angular/common/http';
import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import { map } from 'rxjs/operators';

import { SalesModel } from './sales/sales.model';

import { Api, HttpOptionsAuthorized } from '../../../modules/api/index';

@Injectable({
  providedIn: 'root'
})
export class SalesService {
  private actionUrl: string;

  constructor(private http: HttpClient) {
    this.actionUrl = Api.baseUrl + 'api/v1';
  }

  public getServicesSalesStatisticsByWeek(from: string, to: string){
    return this.getSalesStatistics(from, to, "service/sales/per_week");
  }
  public getServicesSalesStatisticsByMonth(from: string, to: string){
    return this.getSalesStatistics(from, to, "service/sales/per_month");
  }

  public getTripsSalesStatisticsByWeek(from: string, to: string){
    return this.getSalesStatistics(from, to, "trip/sales/per_week");
  }
  public getTripsSalesStatisticsByMonth(from: string, to: string){
    return this.getSalesStatistics(from, to, "trip/sales/per_month");
  }

  public getSalesStatistics(from: string, to: string, type: string){
      let params = new HttpParams().set("from", from).set("to", to);

      return this.http.get<any>(this.actionUrl + "/" + type, 
                                {headers: HttpOptionsAuthorized.headers, 
                                  params: params,
                                observe: HttpOptionsAuthorized.observe})
  }

}
