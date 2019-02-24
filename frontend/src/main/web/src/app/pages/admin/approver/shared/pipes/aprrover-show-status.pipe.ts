import { Pipe, PipeTransform } from '@angular/core';
import {Approver} from '../model/approver';

@Pipe({
  name: 'aprroverShowStatus'
})
export class AprroverShowStatusPipe implements PipeTransform {

  transform(text: boolean): string {
    return text === true ? 'On' : 'Off';
  }
}
