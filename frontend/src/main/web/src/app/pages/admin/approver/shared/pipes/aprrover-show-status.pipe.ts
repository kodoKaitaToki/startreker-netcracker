import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
        name: 'aprroverShowStatus'
      })
export class AprroverShowStatusPipe implements PipeTransform {

  transform(text: boolean): string {
    return text === true ? 'Active' : 'Inactive';
  }
}
