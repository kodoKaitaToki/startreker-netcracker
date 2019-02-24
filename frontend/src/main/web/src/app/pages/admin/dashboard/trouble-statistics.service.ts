import { HttpClient, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpHeaders, HttpParams} from '@angular/common/http';
import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import { map } from 'rxjs/operators';

import { TroubleStatisticsModel } from './trouble-statistics/trouble-statistics.model';

import { Configuration } from '../../../app.constants';

@Injectable({
  providedIn: 'root'
})

export class TroubleStatisticsService {

  private actionUrl: string;

  constructor(private http: HttpClient, private _configuration: Configuration) {
    this.actionUrl = _configuration.ServerWithApiUrl + 'trouble/statistics';
  }

  public getStatistic(): Observable<TroubleStatisticsModel>{
      let headers = new HttpHeaders();
      headers.append('Content-Type', 'application/json');
      //TODO: remove debug backdoor
      headers.append('Authorization', 'debug_login 21');

      return this.http.get<any>(this.actionUrl, {headers})
        .pipe(map(res => {
          return res.amount;
        }))
  }

  public getStatisticForApprover(id: number): Observable<TroubleStatisticsModel>{
      let headers = new HttpHeaders();
      headers = headers.append('Content-Type', 'application/json');
      //TODO: remove debug backdoor
      headers = headers.append('Authorization', 'debug_login 21');

      return this.http.get<any>(this.actionUrl + "/" + id, {headers})
        .pipe(map(res => {
          return res.amount;
        }))
  }
}
