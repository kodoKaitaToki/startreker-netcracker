import {Pipe, PipeTransform} from '@angular/core';
import {Approver} from "../model/approver";

@Pipe({
        name: 'approverFilter',
      })
export class ApproverFilterPipe implements PipeTransform {

  transform(value: Approver[], filterCriteria: string, filterContent: string): Approver[] {

    if (filterCriteria === 'name') {
      filterCriteria = 'username';
    }

    if (filterCriteria === 'status') {
      filterCriteria = 'is_activated';
    }

    if (filterContent.toLowerCase() === 'active') {
      filterContent = 'true';
    }

    if (filterContent.toLowerCase() === 'inactive') {
      filterContent = 'false';
    }

    return value.filter(approver => approver[filterCriteria].toString()
                                                            .toLowerCase()
                                                            .indexOf(filterContent.toLowerCase()) !== -1);
  }
}
