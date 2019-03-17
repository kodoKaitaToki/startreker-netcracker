import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'selectServiceInfo'
})
export class SelectServiceInfoPipe implements PipeTransform {

  transform(inputValue: string): any {
    let returnString: string;

    switch (inputValue) {
      case 'carrierName':
        returnString = 'carrier name';
        break;
      case 'approverName':
        returnString = 'approver name';
        break;
      case 'serviceName':
        returnString = 'service name';
        break;
      case 'serviceDescr':
        returnString = 'service description';
        break;
      case 'serviceStatus':
        returnString = 'service status';
        break;
    }

    return returnString;
  }

}
