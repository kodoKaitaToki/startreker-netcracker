import {Injectable} from "@angular/core";
import {Observable} from "rxjs/internal/Observable";
import {HttpClient} from "@angular/common/http";
import {Api} from "../../../../modules/api";

@Injectable()
export class SearchService {

  private defaultUrl: string = Api.baseUrl;

  private apiVersion: string = 'v1/api/';

  private user: string = 'user/';

  private page: string = 'trips';

  private url: string;

  constructor(private http: HttpClient) {

    this.url = this.defaultUrl + this.apiVersion + this.user + this.page;
  }

  getTrips(departure_planet: string, departure_spaceport: string, departure_date: string, arrival_planet: string, arrival_spaceport: string): Observable<any> {
    return this.http.get(
      this.url + "?" + "departure_planet=" + departure_planet + "&"
      + "departure_spaceport=" + departure_spaceport + "&"
      + "departure_date=" + departure_date + "&"
      + "arrival_planet=" + arrival_planet + "&"
      + "arrival_spaceport=" + arrival_spaceport + "&"
      + "limit=" + 100 + "&"
      + "offset=" + 0
    );
  }

}
