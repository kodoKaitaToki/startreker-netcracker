import {Discount} from "./discount.model";

export class TicketClass {
  class_id: number;

  trip_id: number;

  ticket_price: number;

  class_name: string;

  class_seats: number;

  discount: Discount;
}
