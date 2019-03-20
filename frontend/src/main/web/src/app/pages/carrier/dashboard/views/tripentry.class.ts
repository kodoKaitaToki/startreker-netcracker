export class TripEntry {
  trip_id: number;
  departure_planet_name: string;
  arrival_planet_name: string;

  constructor(trip_id: number, departure_planet_name: string, arrival_planet_name: string) {
    this.trip_id = trip_id;
    this.departure_planet_name = departure_planet_name;
    this.arrival_planet_name = arrival_planet_name;
  }
}
