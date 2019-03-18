import { HttpClient, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpHeaders, HttpParams} from '@angular/common/http';
import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import { map } from 'rxjs/operators';

import { Service } from '../model/service';

import { Api } from 'src/app/modules/api/index';

@Injectable({
  providedIn: 'root'
})
export class ServiceService {
  private actionUrl: string;

  constructor(private http: HttpClient) {
    this.actionUrl = Api.baseUrl + 'api/v1';
  }

  public getServicesForApprover(from: number, number: number, status: number): Observable<Service[]>{
    let headers = new HttpHeaders();
    headers = headers.append('Content-Type', 'application/json');
    //TODO: remove debug backdoor
    headers = headers.append('Authorization', 'debug_login 7');

    let params = new HttpParams()
      .set("from", from.toString())
      .set("number", number.toString())
      .set("status", status.toString());

    return this.http.get<any>(this.actionUrl + "/" + "approver/service", {headers: headers, params: params})
      .pipe(map(res => {

        return res.map(item => {
          return new Service(
              item.id,
              item.approver_name,
              item.service_name,
              item.service_descr,
              item.service_status,
              new Date(item.creation_date),
              item.reply_text
          );
        });
    }))
  }

  public updateServiceReview(service: Service): Observable<Service>{
    let headers = new HttpHeaders();
    headers = headers.append('Content-Type', 'application/json');
    //TODO: remove debug backdoor
    headers = headers.append('Authorization', 'debug_login 7');

    return this.http.put<any>(this.actionUrl + "/" + "approver/service", service, {headers: headers});
  }
}
