import {TicketClass} from "./ticket-class";

export class Trip {
  trip_id : number;
  trip_status: string;
  ticket_classes : TicketClass[];
  departure_spaceport_name: string;
  arrival_spaceport_name: string;
  departure_planet: string;
  arrival_planet: string;
}
