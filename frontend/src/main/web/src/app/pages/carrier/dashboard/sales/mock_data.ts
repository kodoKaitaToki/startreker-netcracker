import { SalesModel } from './sales.model';

export const TRIP_SALES: SalesModel[] = [
  new SalesModel(
    1,
    32588,
    new Date("2018-04-30"),
    new Date("2018-05-06")
  ),
  new SalesModel(
    2,
    57779,
    new Date("2018-05-07"),
    new Date("2018-05-13")
  ),
  new SalesModel(
    2,
    50382,
    new Date("2018-06-11"),
    new Date("2018-06-17")
  )
];

export const SERVICE_SALES: SalesModel[] = [
  new SalesModel(
    3,
    32333,
    new Date("2018-04-30"),
    new Date("2018-05-06")
  ),
  new SalesModel(
    2,
    45001,
    new Date("2018-05-07"),
    new Date("2018-05-13")
  ),
  new SalesModel(
    1,
    56023,
    new Date("2018-06-11"),
    new Date("2018-06-17")
  )
];


