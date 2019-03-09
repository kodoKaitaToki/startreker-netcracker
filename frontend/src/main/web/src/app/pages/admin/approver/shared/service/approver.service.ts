import {Observable} from "rxjs/internal/Observable";
import {HttpClient} from "@angular/common/http";
import {Approver} from "../model/approver";
import {Injectable} from "@angular/core";

import { Api } from '../../../../../modules/api/index';

@Injectable()
export class ApproverService {

  private defaultUrl: string = Api.baseUrl;

  private apiVersion: string = 'api/v1/';

  private user: string = 'admin/';

  private page: string = 'approvers';

  private url: string;

  constructor(private http: HttpClient) {

    this.url = this.defaultUrl + this.apiVersion + this.user + this.page;
  }

  public getCount(): Observable<any> {

    return this.http.get(this.url + '/count');
  }

  public getAll(): Observable<any> {

    return this.http.get(this.url + '/all');
  }

  public getApproversInInterval(limit: number, offset: number): Observable<any> {

    return this.http.get(this.url + '/paging?limit=' + limit + '&offset=' + offset);
  }

  public postApprover(approver: Approver): Observable<any> {

    return this.http.post(this.url, approver);
  }

  public putApprover(approver: Approver): Observable<any> {
    return this.http.put(this.url, approver)
  }

  public deleteApprover(approver: Approver): Observable<any> {

    return this.http.request('DELETE',
                             this.url,
                             {
                               body: approver
                             });
  }
}
