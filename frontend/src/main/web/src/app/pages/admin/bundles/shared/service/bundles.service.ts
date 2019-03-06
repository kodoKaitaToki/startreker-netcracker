import {Observable} from "rxjs/internal/Observable";
import {HttpClient} from "@angular/common/http";
import {Bundles} from "../model/bundles";
import {Injectable} from "@angular/core";

import { Api } from '../../../../../modules/api/index';

@Injectable()
export class BundlesService {

  private defaultUrl: string = Api.baseUrl;

  private apiVersion: string = 'v1/api/';

  private user: string = 'admin/';

  private page: string = 'bundles';

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

  public getBundlesInInterval(limit: number, offset: number): Observable<any> {

    return this.http.get(this.url + '/paging?limit=' + limit + '&offset=' + offset);
  }

  public postBundles(bundles: Bundles): Observable<any> {

    return this.http.post(this.url, bundles);
  }

  public putBundles(bundles: Bundles): Observable<any> {
    return this.http.put(this.url, bundles)
  }

  public deleteBundles(bundles: Bundles): Observable<any> {

    return this.http.request('DELETE',
                             this.url,
                             {
                               body: bundles
                             });
  }
}
