import { PossibleService } from './possible-service';
import { TicketClass } from './ticket-class';

export class Suggestion {
    constructor(
        public id : number,
        public services: PossibleService[],
        public ticketClass: TicketClass
    ) {}
}
  