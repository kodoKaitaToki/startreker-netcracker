import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'selectTripInfo'
})
export class SelectInfoPipe implements PipeTransform {

  transform(inputValue: string): any {
    let returnString: string;

    switch (inputValue) {
      case 'carrierName':
        returnString = 'carrier name';
        break;
      case 'approverName':
        returnString = 'approver name';
        break;
      case 'departureDate':
        returnString = 'departure date';
        break;
      case 'arrivalDate':
        returnString = 'arrival date';
        break;
      case 'departmentPlanetName':
        returnString = 'departure planet';
        break;
      case 'arrivalPlanetName':
        returnString = 'arrival planet';
        break;
      case 'arrivalSpaceportName':
        returnString = 'arrival spaceport';
        break;
      case 'departmentSpaceportName':
        returnString = 'departure spaceport';
        break;
      case 'tripStatus':
        returnString = 'trip status';
        break;
    }

    return returnString;
  }
}
