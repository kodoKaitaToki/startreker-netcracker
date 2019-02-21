import { HttpClient, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
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
      return this.http.post<any>(this.actionUrl, {})
        .pipe(map(res => {
          return res.amount;
        }))
      //TODO: replace with get
  }

  public getStatisticForApprover<T>(id: number): Observable<T>{
      return this.http.post<T>(this.actionUrl + '/' + id, {});
      //TODO: replace with get
  }
}
