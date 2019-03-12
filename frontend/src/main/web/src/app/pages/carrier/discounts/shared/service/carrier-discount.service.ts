import {Observable} from "rxjs/internal/Observable";
import {HttpClient} from "@angular/common/http";
import {Injectable} from "@angular/core";

import {Api} from '../../../../../modules/api/index';

@Injectable()
export class CarrierDiscountsService {

  private url: string = Api.baseUrl + "api/v1/";

  private serviceTypeForDiscount: string = '';

  private userName: string = '?username=mstebles6';

  private getPostOutletApi;

  private deleteOutletApi;

  constructor(private http: HttpClient) {
  }

  public getAll(): Observable<any> {
    return this.http.get(this.getPostOutletApi);
  }

  public post(obj: any): Observable<any> {
    return this.http.post(this.getPostOutletApi, obj);
  }

  public delete() {
    return this.http.delete(this.deleteOutletApi);
  }

  setServiceTypeForDiscount(value: string) {
    this.serviceTypeForDiscount = value;
  }

  buildApiForGetAndPost() {
    this.getPostOutletApi = this.url + this.serviceTypeForDiscount + this.userName;
  }

  buildApiForDelete(id: number) {
    this.deleteOutletApi = `${this.url}${this.serviceTypeForDiscount}/${id}${this.userName}`;
  }
}
