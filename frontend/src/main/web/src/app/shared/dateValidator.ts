import {FormGroup, AbstractControl, FormControl, ValidatorFn } from '@angular/forms';

export const periodError = (from: string, to: string): ValidatorFn => (control: AbstractControl) => {
    const fromDate = new Date(control.get(from).value);
    const toDate = new Date(control.get(to).value);

    if (fromDate > toDate)
        return { periodError: true };
    return null;
}