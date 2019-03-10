import {Service} from "./service";

export class TicketClass {
  class_id : number;
  class_name : string;
  ticket_price : number;
  item_number : number;
  services: Service[];
}
