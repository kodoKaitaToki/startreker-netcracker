export interface Trip {
    departure_id: number;
    arrival_id: number;
    departure_planet_id: number;
    arrival_planet_id: number;
    departure_spaceport_name: string;
    arrival_spaceport_name: string;
    departure_planet_name: string;
    arrival_planet_name: string;
    occurence_count: number;
    percentage: number;
}

export interface TripList {
    trips: Trip[];
}