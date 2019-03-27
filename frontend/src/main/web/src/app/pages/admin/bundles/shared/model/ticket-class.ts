import {Service} from "./service";

export class TicketClass {
  constructor(
    public class_id : number,
    public class_name : string,
    public ticket_price : number,
    public item_number : number,
    public services: Service[]
  ) {}

  getTotalPrice() {
    let servicesPrice: number = this.services
    .map(service => service.getTotalPrice())
    .reduce(( acc, cur ) => acc + cur);
    return ((this.ticket_price || 0) * (this.item_number || 0) + servicesPrice);
  }
}
