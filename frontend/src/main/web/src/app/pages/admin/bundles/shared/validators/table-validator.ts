import { FormControl } from '@angular/forms';

export const emptyTable = (control: FormControl): { [key: string]: any } => {
    const bundleTrips = control.get("bundle_trips").value;

    let ticket_classesExist: boolean = false;

    bundleTrips.forEach(trip => {
        if (trip.ticket_classes.length > 0) {
            ticket_classesExist = true;
        }
    });

    if (ticket_classesExist) return null;

    return { "emptyTable": true };
}