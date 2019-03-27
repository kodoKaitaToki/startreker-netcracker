import { HttpClient, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpHeaders, HttpParams} from '@angular/common/http';
import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import { map } from 'rxjs/operators';

import { Service } from '../model/service';

import { Api, HttpOptionsAuthorized } from 'src/app/modules/api/index';

@Injectable({
  providedIn: 'root'
})
export class ServiceService {
  private actionUrl: string;

  constructor(private http: HttpClient) {
  }

  public getServicesForApprover(from: number, number: number, status: string){
    let params = new HttpParams()
      .set("from", from.toString())
      .set("number", number.toString())
      .set("status", status.toString());

    return this.http.get<any>(Api.service.services(), {headers: HttpOptionsAuthorized.headers, 
                                                                          params: params,
                                                                          observe: HttpOptionsAuthorized.observe})
  }

  public updateServiceReview(service: Service){
    return this.http.put<any>(Api.service.reviewService(service.id), service, HttpOptionsAuthorized);
  }
}
