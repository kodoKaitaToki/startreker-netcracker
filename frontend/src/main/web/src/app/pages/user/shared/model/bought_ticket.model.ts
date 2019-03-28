import {Trip} from '../../../../shared/model/trip.model';
import {FlightClass} from '../../../flights/shared/models/flight-class.model';
import {FlightService} from '../../../flights/shared/models/flight-service.model';

export class BookedTicket{
    trip: Trip;
    ticket: FlightClass;
    services: FlightService[];
    amount: number;
    totalPrice: number;
    is_bought: boolean;

    constructor(trip: Trip,
                ticket: FlightClass,
                services: FlightService[],
                totalPrice: number,
                amount: number){
        this.trip = trip;
        this.ticket = ticket;
        this.services =  services;
      this.totalPrice = totalPrice;
      this.amount = amount;
    }
}
