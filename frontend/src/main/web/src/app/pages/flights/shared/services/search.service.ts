import {Injectable} from "@angular/core";
import {Observable} from "rxjs/internal/Observable";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Api} from "../../../../modules/api";
import {DatePipe} from "@angular/common";

@Injectable()
export class SearchService {

  constructor(private http: HttpClient,
              private datePipe: DatePipe) {
  }

  getTrips(departure_planet: string,
           departure_spaceport: string,
           departure_date: string,
           arrival_planet: string,
           arrival_spaceport: string): Observable<any> {
    let params = new HttpParams()
      .set("departure_planet", departure_planet.toUpperCase())
      .set("departure_spaceport", departure_spaceport)
      .set("departure_date", this.datePipe.transform(departure_date, "yyyy-MM-dd"))
      .set("arrival_planet", arrival_planet.toUpperCase())
      .set("arrival_spaceport", arrival_spaceport);
    return this.http.get(Api.landing.trips(), {params: params});
  }

  getServices(ticket_class_id: number): Observable<any> {
    let params = new HttpParams().set("class-id", ticket_class_id.toString());
    return this.http.get(Api.possibleServices.possibleServices(), {params: params});
  }

}
