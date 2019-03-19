export class Triphistorymodel {
  departure_spaceport_name: string;
  arrival_spaceport_name: string;
  departure_planet_name: string;
  arrival_planet_name: string;
  departure_date: string;
  arrival_date: string;
  carrier_name: string;

  constructor(departure_spaceport_name: string,
    arrival_spaceport_name: string,
    departure_planet_name: string,
    arrival_planet_name: string,
    departure_date: string,
    arrival_date: string,
    carrier_name: string) {
    this.departure_spaceport_name = departure_spaceport_name;
    this.arrival_spaceport_name = arrival_spaceport_name;
    this.departure_planet_name = departure_planet_name;
    this.arrival_planet_name = arrival_planet_name;
    this.departure_date = departure_date;
    this.arrival_date = arrival_date;
    this.carrier_name = carrier_name;
  }
}
