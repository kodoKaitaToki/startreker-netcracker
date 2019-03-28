import {FlightClass} from "../../pages/flights/shared/models/flight-class.model";


export class Trip {

  trip_id: number;

  departure_date: string;

  departure_spaceport: string;

  departure_planet: string;

  arrival_date: string;

  arrival_spaceport: string;

  arrival_planet: string;

  ticket_classes?: FlightClass[];
}
