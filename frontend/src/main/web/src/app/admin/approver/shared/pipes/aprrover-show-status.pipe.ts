import { Pipe, PipeTransform } from '@angular/core';
import {Approver} from '../model/approver';

@Pipe({
  name: 'aprroverShowStatus'
})
export class AprroverShowStatusPipe implements PipeTransform {

  transform(text: string): string {
    return text === 'true' ? 'Activated' : 'Deactivated';
  }
}
