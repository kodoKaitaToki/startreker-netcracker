import {Pipe, PipeTransform} from '@angular/core';
import {PendingTrip} from "../model/pending-trip.model";

@Pipe({
  name: 'itemsFilter'
})
export class ItemsFilter implements PipeTransform {

  transform(trip: PendingTrip[], currentFilterCriteria: string, currentFilterContent: string): PendingTrip[] {

    if (currentFilterContent === 'In draft'.toLowerCase()) {
      currentFilterContent = '1';
    }

    if (currentFilterContent === 'Opened'.toLowerCase()) {
      currentFilterContent = '2';
    }

    if (currentFilterContent === 'Assigned'.toLowerCase()) {
      currentFilterContent = '3';
    }

    if (!currentFilterContent || !currentFilterCriteria) {
      return trip;
    }
    return trip.filter(value => value[currentFilterCriteria].toLowerCase().indexOf(currentFilterContent.toLowerCase()) !== -1);
  }
}
