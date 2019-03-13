import {TicketClass} from "./ticket-class.model";
import {Suggestion} from "./suggestion.model";

export class Trip {

  trip_id: number;

  departure_date: string;

  departure_spaceport_name: string;

  departure_planet_name: string;

  arrival_date: string;

  arrival_spaceport_name: string;

  arrival_planet_name: string;

  ticket_classes?: TicketClass[];

  suggestion?: Suggestion[];
}
