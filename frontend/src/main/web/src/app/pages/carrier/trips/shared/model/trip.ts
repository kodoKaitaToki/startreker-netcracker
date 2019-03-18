import { TicketClass } from 'src/app/pages/admin/bundles/shared/model/ticket-class';

export class Trip {
    trip_id: number;
    departure_planet: string;
    ticket_classes : TicketClass[];
    arrival_planet: string;
    departure_spaceport_name: string;
    arrival_spaceport_name: string;
    departure_date: string;
    arrival_date: string;
    creation_date: string;
  
    constructor() {
  
    }
  }