import {Observable} from "rxjs/internal/Observable";
import {HttpClient} from "@angular/common/http";
import {Bundle} from "../model/bundle";
import {Injectable} from "@angular/core";

import { Api } from '../../../../../modules/api/index';
import { BundleForm } from '../model/bundle-form';

@Injectable()
export class BundlesService {

  private defaultUrl: string = Api.baseUrl;

  private apiVersion: string = 'api/v1/';

  private page: string = 'bundles';

  private url: string;

  constructor(private http: HttpClient) {

    this.url = this.defaultUrl + this.apiVersion + this.page;
  }

  public getCount(): Observable<any> {

    return this.http.get(this.url + '/count');
  }

  public getAll(): Observable<any> {

    return this.http.get(this.url);
  }

  public getBundlesInInterval(limit: number, offset: number): Observable<any> {

    return this.http.get(this.url + '?limit=' + limit + '&offset=' + offset);
  }

  public postBundle(bundle: BundleForm): Observable<any> {

    return this.http.post(this.url, bundle);
  }

  public putBundle(bundle: BundleForm): Observable<any> {
    return this.http.put(this.url + `/${bundle.id}`, bundle)
  }

  public deleteBundle(bundle: Bundle): Observable<any> {

    return this.http.request('DELETE',
                             this.url + `/${bundle.id}`,
                             {
                               body: bundle
                             });
  }
}
