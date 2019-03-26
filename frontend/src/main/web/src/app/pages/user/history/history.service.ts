import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {map} from "rxjs/operators";
import {Api, checkToken, HttpOptionsAuthorized} from "../../../modules/api";
import {HistoryElement} from "./historyelement.model";
import {Triphistorymodel} from "./triphistorymodel.model";
import {Servicehistorymodel} from "./servicehistorymodel.model";

@Injectable({
  providedIn: 'root'
})
export class HistoryService {

  private _totalItemsCallback;

  constructor(private http: HttpClient) {
  }

  public getUserTicketHistory(offset: number, limit: number, from: string, to: string) {

    let params;
    params = new HttpParams()
      .set("limit", limit.toString())
      .set("offset", offset.toString());

    if (from && to) {
      params = params.set("start-date", from);
      params = params.set("end-date", to);
    }

    return this.http.get<any>(Api.history.getBoughtTickets(),
      {headers: HttpOptionsAuthorized.headers, params: params, observe: HttpOptionsAuthorized.observe}
    )
               .pipe(map(res => {

                 checkToken(res.headers);
                 if(this._totalItemsCallback) this._totalItemsCallback(res.headers.get("X-Total-Count"));

                 return res.body.map(item => {
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

    return this.http.get<any>(Api.history.getTicketService(ticketId),
      {headers: HttpOptionsAuthorized.headers, observe: HttpOptionsAuthorized.observe}
    )
               .pipe(map(res => {

                 checkToken(res.headers);

                 return res.body.map(item => {
                   return new Servicehistorymodel(item.bought_services_name, item.bought_services_count);
                 });
               }))
  }

  set totalItemsCallback(value) {
    this._totalItemsCallback = value;
  }
}
