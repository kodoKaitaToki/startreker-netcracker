import {Pipe, PipeTransform} from '@angular/core';
import {Service} from "../model/service.model";

@Pipe({
  name: 'serviceFilter',
})
export class ServiceFilterPipe implements PipeTransform {

  transform(value: Service[], filterContent: string): Service[] {
    return value.filter(service => service.service_name.toLowerCase()
                                          .indexOf(filterContent.toLowerCase()) !== -1);
  }
}
