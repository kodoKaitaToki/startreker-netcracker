import {Observable} from "rxjs/internal/Observable";
import {HttpClient} from "@angular/common/http";
import {Approver} from "../model/approver";
import {Injectable} from "@angular/core";

import {Api, HttpOptionsAuthorized} from '../../../../../modules/api/index';

@Injectable()
export class ApproverService {

  private defaultUrl: string = Api.baseUrl;

  private apiVersion: string = 'v1/api/';

  private user: string = 'admin/';

  private page: string = 'approvers';

  private url: string;

  constructor(private http: HttpClient) {

    this.url = this.defaultUrl + this.apiVersion + this.user + this.page;
  }

  public getCount(): Observable<any> {

    return this.http.get ( this.url + '/count', HttpOptionsAuthorized );
  }

  public getAll(): Observable<any> {

    return this.http.get ( this.url + '/all', HttpOptionsAuthorized );
  }

  public getApproversInInterval(limit: number, offset: number): Observable<any> {

    return this.http.get ( this.url + '/paging?limit=' + limit + '&offset=' + offset, HttpOptionsAuthorized );
  }

  public postApprover(approver: Approver): Observable<any> {

    return this.http.post ( this.url, approver, HttpOptionsAuthorized );
  }

  public putApprover ( approver: Approver ): Observable<any> {
    return this.http.put ( this.url, approver, HttpOptionsAuthorized )
  }

  public deleteApprover(approver: Approver): Observable<any> {

    return this.http.request ( 'DELETE',
                               this.url,
                               {
                                 body: approver,
                                 headers:  HttpOptionsAuthorized.headers
                               }
    );
  }
}
