import { HttpClient, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpHeaders, HttpParams} from '@angular/common/http';
import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import { map } from 'rxjs/operators';

import { Configuration } from '../../../app.constants';

@Injectable({
  providedIn: 'root'
})
export class DashboardDeltaService {
  private actionUrl: string;

  constructor(private http: HttpClient, private _configuration: Configuration) {
    this.actionUrl = _configuration.ServerWithApiUrl + 'v1/admin/increasing';
  }

  public getUsersIncreasingPerPeriod(from: string, to: string): Observable<Map<string, number>>{
      let headers = new HttpHeaders();
      headers.append('Content-Type', 'application/json');

      let params = new HttpParams().set("from", from).set("to", to);

      return this.http.get<any>(this.actionUrl + "/users", {headers: headers, params: params})
        .pipe(map(res => {
          let map = new Map();
          for (let key of Object.keys(res)) {
            map.set(key, res[key]);
          }
          return map;
      }))
  }
}
