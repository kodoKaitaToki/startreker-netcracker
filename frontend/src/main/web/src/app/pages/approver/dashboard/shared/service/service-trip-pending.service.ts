import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Api} from "../../../../../modules/api";
import {Observable} from "rxjs/internal/Observable";

@Injectable()
export class ServiceTripPendingService {

  private api = Api.baseUrl + "api/v1/approver/";

  constructor(private http: HttpClient) {
  }

  public getAllPendingTrips(): Observable<any> {
    return this.http.get(this.api + 'all-pending-trips');
  }

  public getAllPendingServices(): Observable<any> {
    return this.http.get(this.api + 'all-pending-services');
  }
}
