import { FormControl, AbstractControl, ValidatorFn } from '@angular/forms';

export class DateValidator {

    static notEarlierThanCurrentDate(control: FormControl): { [key: string]: any } {
        const startDate = new Date(control.value);
        const currentDate = new Date();
        if (+startDate < +currentDate)
            return { "notEarlierThanCurrentDate": true };

        return null;
    }

    // static dateLessThan(dateField1: string, dateField2: string, validatorField: { [key: string]: boolean }): ValidatorFn {
    //     return (c: AbstractControl): { [key: string]: boolean } | null => {
    //         const date1 = new Date(c.get(dateField1).value);
    //         const date2 = new Date(c.get(dateField2).value);
    //         if ((date1 !== null && date2 !== null) && +date1 > +date2) {
    //             return { "dateLessThan": true };
    //         }
    //         return null;
    //     };
    // }
}

