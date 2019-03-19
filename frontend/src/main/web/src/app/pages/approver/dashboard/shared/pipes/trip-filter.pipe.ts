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
    return trip.filter(value => value[currentFilterCriteria].toLowerCase().indexOf(currentFilterContent.toLowerCase()) !== -1);
  }
}
