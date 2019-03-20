import {TripForm} from "./trip-form";

export class BundleForm {
  constructor(
    public id: number,

    public start_date: string,

    public finish_date: string,

    public bundle_price: number,

    public bundle_description: string,

    public photo_uri: string,

    public bundle_trips: TripForm[],
  ) {}
}
