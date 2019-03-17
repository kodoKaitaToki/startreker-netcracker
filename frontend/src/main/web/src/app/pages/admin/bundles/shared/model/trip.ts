import {TicketClass} from "./ticket-class";

export class Trip {
  constructor(
    public trip_id : number,
    public trip_status: string,
    public ticket_classes : TicketClass[],
    public departure_spaceport_name: string,
    public arrival_spaceport_name: string,
    public departure_planet: string,
    public arrival_planet: string
  ) {}

  getTotalPrice() {
    let classesPrice: number = this.ticket_classes
    .map(ticketClass => ticketClass.getTotalPrice())
    .reduce(( acc, cur ) => acc + cur);
    return (classesPrice);
  }
}
