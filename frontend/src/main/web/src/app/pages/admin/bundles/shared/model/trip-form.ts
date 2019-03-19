import {TicketClassForm} from "./ticket-class-form";

export class TripForm {
  constructor(
    public trip_id : number,
    public ticket_classes : TicketClassForm[],
  ) {}
}
