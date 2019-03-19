import { TripReply } from './trip_reply.model';
import { Spaceport } from './spaceport.model';

export class Trip{
    trip_id: Number;
    carrier_id: Number;
    carrier_name: String;
    approver_id: Number;
    departure_spaceport: Spaceport;
    arival_spaceport: Spaceport;
    trip_status: String;
    departure_date: Date;
    arrival_date: Date;
    creation_date: Date;
    trip_reply: TripReply[];
    trip_photo: String;
}