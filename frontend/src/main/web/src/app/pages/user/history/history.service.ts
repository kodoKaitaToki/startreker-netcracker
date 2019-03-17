import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {map} from "rxjs/operators";
import {Api} from "../../../modules/api";
import {HistoryElement} from "./historyelement.model";
import {Triphistorymodel} from "./triphistorymodel.model";
import {Servicehistorymodel} from "./servicehistorymodel.model";

@Injectable({
  providedIn: 'root'
})
export class HistoryService {

  private actionUrl: string;

  constructor(private http: HttpClient) {
    this.actionUrl = Api.baseUrl + 'api/v1';
  }

  public getUserTicketHistory(offset: number, limit: number, from: string, to: string) {
    let headers = new HttpHeaders();
    headers = headers.append('Content-Type', 'application/json');
    //TODO: remove debug backdoor
    headers = headers.append('Authorization', 'debug_login 20');

    let params;
    params = new HttpParams()
      .set("limit", limit.toString())
      .set("offset", offset.toString());

    if(from) params = params.set("start-date", from);
    if(to) params = params.set("end-date", to);

    return this.http.get<any>(this.actionUrl + "/history/user/ticket", {headers: headers, params: params})
               .pipe(map(res => {

                 return res.map(item => {
                   return new HistoryElement(
                     item.id,
                     item.purchase_date,
                     item.end_price,
                     item.seat,
                     item.bundle_id,
                     item.class_name,
                     new Triphistorymodel(
                       item.trip.departure_spaceport_name,
                       item.trip.arrival_spaceport_name,
                       item.trip.departure_planet_name,
                       item.trip.arrival_planet_name,
                       item.trip.departure_date,
                       item.trip.arrival_date,
                       item.trip.carrier_name
                     ),
                     undefined
                   );
                 });
               }))
  }

  public getTicketServices(ticketId: number) {
    let headers = new HttpHeaders();
    headers = headers.append('Content-Type', 'application/json');
    //TODO: remove debug backdoor
    headers = headers.append('Authorization', 'debug_login 21');

    return this.http.get<any>(this.actionUrl + "/history/ticket/" + ticketId + "/service", {headers: headers})
               .pipe(map(res => {

                 return res.map(item => {
                   return new Servicehistorymodel(item.bought_services_name);
                 });
               }))
  }
}
