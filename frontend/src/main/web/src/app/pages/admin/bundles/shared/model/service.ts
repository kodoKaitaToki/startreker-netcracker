export class Service {
  constructor(
    public service_id : number,
    public service_name : string,
    public service_price : number,
    public item_number : number,
  ) {}

  getTotalPrice() {
    return ((this.service_price || 0) * (this.item_number || 0));
  }
}
