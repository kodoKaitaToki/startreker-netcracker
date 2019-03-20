import {Injectable} from "@angular/core";
import {Observable} from "rxjs/internal/Observable";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Api} from "../../../../modules/api";

@Injectable()
export class SearchService {
  
  constructor(private http: HttpClient) {}

  getTrips(departure_planet: string, 
          departure_spaceport: string, 
          departure_date: string, 
          arrival_planet: string, 
          arrival_spaceport: string): Observable<any> {
    let params = new HttpParams()
        .set("departure_planet", departure_planet)
        .set("departure_spaceport", departure_spaceport)
        .set("departure_date", departure_date)
        .set("arrival_planet", arrival_planet)
        .set("arrival_spaceport", arrival_spaceport);   
    return this.http.get(Api.landing.trips(), {params: params});
  }

}
