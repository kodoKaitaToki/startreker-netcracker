import { HttpClient, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpHeaders, HttpParams} from '@angular/common/http';
import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import { map } from 'rxjs/operators';

import { Api } from '../../../modules/api/index';

@Injectable({
  providedIn: 'root'
})
export class DashboardDeltaService {
  private actionUrl: string;

  constructor(private http: HttpClient) {
    this.actionUrl = Api.baseUrl + 'api/v1/admin/increasing';
  }

  public getUsersIncreasingPerPeriod(from: string, to: string): Observable<Map<Date, number>>{
      return this.getIncreasingPerPeriod(from, to, "users")
  }

  public getCarriersIncreasingPerPeriod(from: string, to: string): Observable<Map<Date, number>>{
      return this.getIncreasingPerPeriod(from, to, "carriers")
  }

  public getLocationsIncreasingPerPeriod(from: string, to: string): Observable<Map<Date, number>>{
      return this.getIncreasingPerPeriod(from, to, "locations")
  }

  public getIncreasingPerPeriod(from: string, to: string, type: string): Observable<Map<Date, number>>{
      let headers = new HttpHeaders();
      headers = headers.append('Content-Type', 'application/json');
      //TODO: remove debug backdoor
      headers = headers.append('Authorization', 'debug_login 21');

      let params = new HttpParams().set("from", from).set("to", to);

      return this.http.get<any>(this.actionUrl + "/" + type, {headers: headers, params: params})
        .pipe(map(res => {
          let map = new Map();
          for (let key of Object.keys(res)) {
            map.set(new Date(key), res[key]);
          }
          map = new Map([...map.entries()].sort(function compare(a, b) {
            var dateA = new Date(a[0]);
            var dateB = new Date(b[0]);
            return dateA.getTime() - dateB.getTime();
          }));

          return map;
      }))
  }
}
