import {Pipe, PipeTransform} from '@angular/core';
import {Bundle} from "../model/bundle";

@Pipe({
        name: 'bundlesFilter',
      })
export class BundlesFilterPipe implements PipeTransform {

  transform(value: Bundle[], filterCriteria: string, filterContent: string): Bundle[] {

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
