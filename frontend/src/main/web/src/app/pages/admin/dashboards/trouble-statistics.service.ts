import { HttpClient, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpHeaders, HttpParams} from '@angular/common/http';
import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import { map } from 'rxjs/operators';

import { TroubleStatisticsModel } from './trouble-statistics/trouble-statistics.model';

import { Api } from '../../../modules/api/index';

@Injectable({
  providedIn: 'root'
})

export class TroubleStatisticsService {

  private actionUrl: string;

  constructor(private http: HttpClient) {
    this.actionUrl = Api.baseUrl + 'api/v1/trouble/statistics';
  }

  public getStatistic(): Observable<TroubleStatisticsModel>{
      let headers = new HttpHeaders();
      headers.append('Content-Type', 'application/json');

      return this.http.get<any>(this.actionUrl, {headers})
        .pipe(map(res => {
          return res.amount;
        }))
  }

  public getStatisticForApprover(id: number): Observable<TroubleStatisticsModel>{
      let headers = new HttpHeaders();
      headers = headers.append('Content-Type', 'application/json');

      return this.http.get<any>(this.actionUrl + "/" + id, {headers})
        .pipe(map(res => {
          return res.amount;
        }))
  }
}
