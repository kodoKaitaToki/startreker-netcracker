import {Triphistorymodel} from "./triphistorymodel.model";
import {Servicehistorymodel} from "./servicehistorymodel.model";

export class HistoryElement {
  id: number;
  purchase_date: string;
  end_price: number;
  seat: number;
  bundle_id: number;
  class_name: string;

  trip: Triphistorymodel;
  services: Servicehistorymodel;

  constructor(id: number,
    purchase_date: string,
    end_price: number,
    seat: number,
    bundle_id: number,
    class_name: string,
    trip: Triphistorymodel,
    services: Servicehistorymodel) {
    this.id = id;
    this.purchase_date = purchase_date;
    this.end_price = end_price;
    this.seat = seat;
    this.bundle_id = bundle_id;
    this.class_name = class_name;
    this.trip = trip;
    this.services = services;
  }
}
