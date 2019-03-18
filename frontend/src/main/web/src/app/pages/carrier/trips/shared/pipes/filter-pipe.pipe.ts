import {Pipe, PipeTransform} from '@angular/core';
import {Trip} from "../model/trip";

@Pipe({
        name: 'tripFilter',
      })
export class TripFilterPipe implements PipeTransform {

  transform(value: Trip[], filterCriteria: string, filterContent: string): Trip[] {

    // if (filterCriteria === 'status') {
    //   filterCriteria = 'trip_status';
    // }

    // if (filterCriteria === 'departure planet') {
    //   filterCriteria = 'departure_planet';
    // }

    // if (filterCriteria === 'arrival planet') {
    //   filterCriteria = 'arrival_planet';
    // }

    // if (filterCriteria === 'departure date') {
    //   filterCriteria = 'departure_date';
    // }
    
    // if (filterCriteria === 'arrival date') {
    //   filterCriteria = 'arrival_date';
    // }


    return value.filter(trip => trip[filterCriteria].toString()
                                                            .toLowerCase()
                                                            .indexOf(filterContent.toLowerCase()) !== -1);
  }
}
