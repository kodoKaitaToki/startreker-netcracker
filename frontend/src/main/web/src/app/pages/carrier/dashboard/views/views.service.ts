import {
  HttpClient,
  HttpParams
} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {map} from 'rxjs/operators';
import {Api, checkToken, HttpOptionsAuthorized} from "../../../../modules/api";
import {ViewsModel} from "./viewsmodel.model";
import {TripEntry} from "./tripentry.class";
import {ServiceEntry} from "./serviceentry.class";

@Injectable({
  providedIn: 'root'
})
export class ViewsService {
  private actionUrl: string;

  constructor(private http: HttpClient) {
    this.actionUrl = Api.baseUrl + 'api/v1';
  }

  public getServicesViewsStatisticsByWeek(from: string, to: string) {
    return this.getViewsStatistics(from, to, "service/views/per_week");
  }

  public getServicesViewsStatisticsByMonth(from: string, to: string) {
    return this.getViewsStatistics(from, to, "service/views/per_month");
  }

  public getTripsViewsStatisticsByWeek(from: string, to: string) {
    return this.getViewsStatistics(from, to, "trip/views/per_week");
  }

  public getTripsViewsStatisticsByMonth(from: string, to: string) {
    return this.getViewsStatistics(from, to, "trip/views/per_month");
  }

  public getServicesViewsStatisticsByWeekByService(serviceId: number, from: string, to: string) {
    return this.getViewsStatistics(from, to, "service/" + serviceId + "/views/per_week");
  }

  public getServicesViewsStatisticsByMonthByService(serviceId: number, from: string, to: string) {
    return this.getViewsStatistics(from, to, "service/" + serviceId + "/views/per_month");
  }

  public getTripsViewsStatisticsByWeekByTrip(tripId: number, from: string, to: string) {
    return this.getViewsStatistics(from, to, "trip/" + tripId + "/views/per_week");
  }

  public getTripsViewsStatisticsByMonthByTrip(tripId: number, from: string, to: string) {
    return this.getViewsStatistics(from, to, "trip/" + tripId + "/views/per_month");
  }

  public getViewsStatistics(from: string, to: string, type: string) {
    let params = new HttpParams().set("from", from)
                                 .set("to", to);

    return this.http.get<any>(this.actionUrl + "/" + type,
      {
        headers: HttpOptionsAuthorized.headers,
        params: params,
        observe: HttpOptionsAuthorized.observe
      }
    )
               .pipe(map(res => {
                 checkToken(res.headers);

                 return res.body.map(item => {
                   return new ViewsModel(
                     item.views,
                     new Date(item.from),
                     new Date(item.to),
                   );
                 });
               }))
  }

  public getCarrierTrips() {

    return this.http.get<any>(this.actionUrl + "/carrier/trip",
      {
        headers: HttpOptionsAuthorized.headers,
        observe: HttpOptionsAuthorized.observe
      }
    )
               .pipe(map(res => {
                 checkToken(res.headers);

                 return res.body.map(item => {
                   return new TripEntry(
                     item.trip_id,
                     item.departure_spaceport.planet.planet_name,
                     item.arrival_spaceport.planet.planet_name
                   );
                 });
               }))
  }

  public getCarrierServices() {

    return this.http.get<any>(this.actionUrl + "/carrier/services/preload",
      {
        headers: HttpOptionsAuthorized.headers,
        observe: HttpOptionsAuthorized.observe
      }
    )
               .pipe(map(res => {
                 checkToken(res.headers);

                 return res.body.map(item => {
                   return new ServiceEntry(
                     item.service_id,
                     item.service_name
                   );
                 });
               }))
  }

}
