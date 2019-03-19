import {ServiceForm} from "./service-form";

export class TicketClassForm {
  constructor(
    public ticket_class_id : number,
    public item_number : number,
    public services: ServiceForm[]
  ) {}
}
