import {Pipe, PipeTransform} from "@angular/core";

@Pipe({
  name: 'statePipe'
})
export class StatePipe implements PipeTransform {

  transform(inputState: number): string {
    let returnValue: string;

    switch (inputState) {
      case 1:
        returnValue = 'Draft';
        break;
      case 2:
        returnValue = 'Open';
        break;
      case 3:
        returnValue = 'Assgined';
        break;
    }

    return returnValue;
  }
}
