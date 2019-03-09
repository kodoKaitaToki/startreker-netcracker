import {TicketClass} from "./ticker-class";

export class Trip {
  trip_id : number;
  ticket_classes : TicketClass[];
  departure_spaceport_name: string;
  arrival_spaceport_name: string;
  departure_planet: string;
  arrival_planet: string;
}
