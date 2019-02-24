export class SalesModel {
  sold: number;
  revenue: number;
  from: Date;
  to: Date;

  constructor(sold, revenue, from, to)
  {
    this.sold = sold;
    this.revenue = revenue;
    this.from = from;
    this.to = to;
  }
}
