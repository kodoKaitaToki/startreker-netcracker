import { Pipe, PipeTransform } from '@angular/core';
import {Carrier} from './carrier';

@Pipe({
  name: 'carrierShowStatus'
})
export class CarrierShowStatusPipe implements PipeTransform {

  transform(text: boolean): string {
    return text === true ? 'On' : 'Off';
  }
}