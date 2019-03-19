import {PossibleService} from "./possible-service";

export class TicketClass {
  constructor(
    public class_id : number,
    public class_name : string,
    public ticket_price : number,
    public services: PossibleService[]
  ) {}
}
