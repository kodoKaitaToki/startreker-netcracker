import {Discount} from "./discount.model";

export class Suggestion {
  suggestion_id: number;

  class_id: number;

  discount: Discount;

  ticket_price: number;

  class_name: string;

  class_seats: number;

  service_names: string[];
}
