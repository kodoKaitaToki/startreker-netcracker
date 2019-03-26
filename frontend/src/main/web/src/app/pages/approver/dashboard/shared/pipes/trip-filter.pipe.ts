import {Pipe, PipeTransform} from '@angular/core';
import {PendingTrip} from "../model/pending-trip.model";

@Pipe({
  name: 'itemsFilter'
})
export class ItemsFilter implements PipeTransform {

  transform(trip: PendingTrip[], currentFilterCriteria: string, currentFilterContent: string): PendingTrip[] {

    if (!currentFilterContent || !currentFilterCriteria) {
      return trip;
    }

    switch (currentFilterContent) {
      case 'draft':
        currentFilterContent = '1';
        break;
      case 'open':
        currentFilterContent = '2';
        break;
      case 'assigned':
        currentFilterContent = '3';
        break;
    }

    return trip.filter(value => value[currentFilterCriteria].toString().toLowerCase().indexOf(currentFilterContent.toLowerCase()) !== -1);
  }
}
