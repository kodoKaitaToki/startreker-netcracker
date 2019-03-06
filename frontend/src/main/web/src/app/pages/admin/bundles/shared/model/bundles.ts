import {Services} from "./services";
import {Trips} from "./trips";

export class Bundles {
  id: number;

  start_date: Date;

  finish_date: Date;

  price: number;

  description: string;

  trips: Trips;

  services: Services;

  constructor() {

  }
}
