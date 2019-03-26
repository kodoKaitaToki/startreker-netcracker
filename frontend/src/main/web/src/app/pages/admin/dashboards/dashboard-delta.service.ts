import { HttpClient, HttpParams, HttpHeaders} from '@angular/common/http';
import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';

import {Api, HttpOptionsAuthorized} from '../../../modules/api/index';

@Injectable({
  providedIn: 'root'
})
export class DashboardDeltaService {
  private actionUrl: string;
  private tokens;

  constructor(private http: HttpClient) {
    this.actionUrl = Api.baseUrl + 'api/v1/admin/increasing';
  }

  public getUsersIncreasingPerPeriod(from: string, to: string): Observable<any>{
      return this.getIncreasingPerPeriod(from, to, "users")
  }

  public getCarriersIncreasingPerPeriod(from: string, to: string): Observable<any>{
      return this.getIncreasingPerPeriod(from, to, "carriers")
  }

  public getLocationsIncreasingPerPeriod(from: string, to: string): Observable<any>{
      return this.getIncreasingPerPeriod(from, to, "locations")
  }

  public getIncreasingPerPeriod(from: string, to: string, type: string): Observable<any>{
      /*let headers = new HttpHeaders();
      headers = headers.append('Content-Type', 'application/json');*/

      let params = new HttpParams().set("from", from).set("to", to);
      let headers = new HttpHeaders({
        'Content-Type':  'application/json',
        'Authorization': `Bearer ${localStorage.getItem('at')}`,
        'Authorization-Refresh': `Bearer ${localStorage.getItem('rt')}`
      })

      return this.http.get<any>(this.actionUrl + "/" + type, {
          headers: headers,
          params: params,
          observe: 'response' as 'response'
        })
      /*resp = resp
        .pipe(map(res => {
          let map = new Map();
          for (let key of Object.keys(res.body)) {
            map.set(new Date(key), res.body[key]);
          }
          map = new Map([...map.entries()].sort(function compare(a, b) {
            var dateA = new Date(a[0]);
            var dateB = new Date(b[0]);
            return dateA.getTime() - dateB.getTime();
          }));

          return map;
      }))
      return resp;*/
  }
}
