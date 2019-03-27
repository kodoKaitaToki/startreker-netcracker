export class Trip {
    trip_id: number;
    trip_status: string;
    departure_planet: string;
    arrival_planet: string;
    departure_spaceport: string;
    arrival_spaceport: string;
    departure_date: string;
    arrival_date: string;
    creation_date: string;
    arrival_time?: string;
    departure_time?: string;
    trip_status_id: number;
    trip_reply: string;
    ticket_classes: object[];

    constructor() {

    }
  }
