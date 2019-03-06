import {Pipe, PipeTransform} from '@angular/core';
import {Bundles} from "../model/bundles";

@Pipe({
        name: 'bundlesFilter',
      })
export class BundlesFilterPipe implements PipeTransform {

  transform(value: Bundles[], filterCriteria: string, filterContent: string): Bundles[] {

    if (filterCriteria === 'id') {
      filterCriteria = 'id';
    }

    if (filterCriteria === 'price') {
      filterCriteria = 'price';
    }

/*    if (filterContent.toLowerCase() === 'active') {
      filterContent = 'true';
    }

    if (filterContent.toLowerCase() === 'inactive') {
      filterContent = 'false';
    }
*/
    return value.filter(bundles => bundles[filterCriteria].toString()
                                                            .toLowerCase()
                                                            .indexOf(filterContent.toLowerCase()) !== -1);
  }
}
