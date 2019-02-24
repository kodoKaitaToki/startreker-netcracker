import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
        name: 'filter'
      })
export class FilterPipePipe implements PipeTransform {

  transform(object: any[], fieldName: string, searchStr: string): any {

    if (fieldName === 'name') {
      fieldName = 'username';
    }

    if (fieldName === 'status') {
      fieldName = 'is_activated';
    }

    if (searchStr.toLowerCase() === 'on') {
      searchStr = 'true';
    }

    if (searchStr.toLowerCase() === 'off') {
      searchStr = 'false';
    }

    return object.filter(obj => obj[fieldName].toString()
                                              .toLowerCase()
                                              .indexOf(searchStr.toString()
                                                                .toLowerCase()) !== -1);
  }
}
