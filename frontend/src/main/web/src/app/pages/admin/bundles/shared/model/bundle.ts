import {Trip} from "./trip";

export class Bundle {
  id: number;

  start_date: Date;

  finish_date: Date;

  bundle_price: number;

  bundle_description: string;

  bundle_trips: Trip[];

  constructor() {

  }
}
