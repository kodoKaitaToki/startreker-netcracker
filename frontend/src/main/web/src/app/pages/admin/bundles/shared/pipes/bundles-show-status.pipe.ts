import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
        name: 'bundlesShowStatus'
      })
export class BundlesShowStatusPipe implements PipeTransform {

  transform(text: boolean): string {
    return text === true ? 'Active' : 'Inactive';
  }
}
