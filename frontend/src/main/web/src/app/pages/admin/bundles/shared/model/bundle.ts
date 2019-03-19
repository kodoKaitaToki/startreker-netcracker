import {Trip} from "./trip";

export class Bundle {
  constructor(
    public id: number,

    public start_date: Date,

    public finish_date: Date,

    public bundle_price: number,

    public bundle_description: string,

    public bundle_trips: Trip[],
  ) {}
}
