import { Component, Input, OnInit, OnChanges, SimpleChanges, EventEmitter, Output } from '@angular/core';
import {FormControl, FormGroup, Validators, ValidatorFn} from '@angular/forms';

@Component({
  selector: 'app-trips-ticket-class',
  templateUrl: './trips-ticket-class.component.html',
  styleUrls: ['./trips-ticket-class.component.scss']
})
export class TripsTicketClassComponent implements OnInit, OnChanges {

  @Input() ticketClasses:any = [];
  @Input() trip_id:any;
  @Input() uneditTicketClasses: any;
  @Output() submitFormEventEmitter$ = new EventEmitter();
  @Output() deleteFormEventEmitter$ = new EventEmitter();
  ticketClassesDefault:any = [
    {
      class_name: "economy",
      class_seats: 0,
      remaining_seats: 0,
      ticket_price: 0,
    },
    {
      class_name: "first",
      class_seats: 0,
      remaining_seats: 0,
      ticket_price: 0,
    },
    {
      class_name: "business",
      class_seats: 0,
      remaining_seats: 0,
      ticket_price: 0,
    }
  ];
  actualTicketClasses: any = [];
  type: string;

  formEconomy: FormGroup;
  formFirst: FormGroup;
  formBusiness: FormGroup;

  isEconomyDelete: boolean;
  isFirstDelete: boolean;
  isBusinessDelete: boolean;


  constructor() { }

  mergeTicketClasses(change) {
    this.actualTicketClasses = this.ticketClassesDefault.map(el => {
      change.map(actualEl => {
        if (actualEl.class_name === el.class_name) {
          el = actualEl;
        }
      })
      return el;
    });
  }


  setFormInDefault() {
    this.formEconomy = new FormGroup(
      {
        class_name: new FormControl({value: '',disabled: true}, Validators.required),
        class_seats: new FormControl({value: 0,disabled: this.uneditTicketClasses}, [Validators.required, Validators.min(1)]),
        remaining_seats: new FormControl({value: 0,disabled: true}, Validators.required),
        ticket_price: new FormControl({value: 0,disabled: this.uneditTicketClasses}, [Validators.required, Validators.min(1)]),
      }
    );

    this.formFirst = new FormGroup(
      {
        class_name: new FormControl({value: '',disabled: true}, Validators.required),
        class_seats: new FormControl({value: 0,disabled: this.uneditTicketClasses}, [Validators.required, Validators.min(1)]),
        remaining_seats: new FormControl({value: 0,disabled: true}, Validators.required),
        ticket_price: new FormControl({value: 0,disabled: this.uneditTicketClasses}, [Validators.required, Validators.min(1)]),
      }
    );

    this.formBusiness = new FormGroup(
      {
        class_name: new FormControl({value: '',disabled: true}, Validators.required),
        class_seats: new FormControl({value: 0,disabled: this.uneditTicketClasses}, [Validators.required, Validators.min(1)]),
        remaining_seats: new FormControl({value: 0,disabled: true}, Validators.required),
        ticket_price: new FormControl({value: 0,disabled: this.uneditTicketClasses}, [Validators.required, Validators.min(1)]),
      }
    );
  }

  submitEconomy() {
    const payload = {
      ...this.formEconomy.getRawValue(),
      trip_id: this.trip_id,
    };
    delete payload.remaining_seats;
    this.submitFormEventEmitter$.emit(payload);
  }

  clearEconomyForm() {
    if (!this.isEconomyDelete) {
      console.log(this.isEconomyDelete)
      this.formEconomy.patchValue({
        class_name: 'economy',
        class_seats: 0,
        remaining_seats: 0,
        ticket_price: 0,
      });
      return;
    }
    const deleteEntity = this.actualTicketClasses.find(el => el.class_name === 'economy');

    this.deleteFormEventEmitter$.emit(deleteEntity.class_id);
  }

  submitFirst() {
    const payload = {
      ...this.formFirst.getRawValue(),
      trip_id: this.trip_id,
    };
    delete payload.remaining_seats;
    this.submitFormEventEmitter$.emit(payload);
  }

  clearFirstForm() {
    if (!this.isFirstDelete) {
      this.formFirst.patchValue({
        class_name: 'first',
        class_seats: 0,
        remaining_seats: 0,
        ticket_price: 0,
      });
      return;
    }

    const deleteEntity = this.actualTicketClasses.find(el => el.class_name === 'first');

    this.deleteFormEventEmitter$.emit(deleteEntity.class_id);
  }

  submitBusiness() {
    const payload = {
      ...this.formBusiness.getRawValue(),
      trip_id: this.trip_id,
    };
    delete payload.remaining_seats;
    this.submitFormEventEmitter$.emit(payload);
  }

  clearBusinessForm() {
    if (!this.isBusinessDelete) {
      this.formBusiness.patchValue({
        class_name: 'business',
        class_seats: 0,
        remaining_seats: 0,
        ticket_price: 0,
      });
      return;
    }

    const deleteEntity = this.actualTicketClasses.find(el => el.class_name === 'business');

    this.deleteFormEventEmitter$.emit(deleteEntity.class_id);
  }

  ngOnInit() {
  }

  ngOnChanges(changes: SimpleChanges) {
    this.setFormInDefault();
    this.mergeTicketClasses(changes.ticketClasses.currentValue);
    this.actualTicketClasses.forEach(el => {
      if (el.class_name === 'economy') {
        this.formEconomy.patchValue({
          class_name: el.class_name,
          class_seats: el.class_seats,
          remaining_seats: 0,
          ticket_price: el.ticket_price,
        });

        if (el.hasOwnProperty('class_id')) {
          this.isEconomyDelete = true;
        } else {
          this.isEconomyDelete = false;
        }
      }

      if (el.class_name === 'first') {
        this.formFirst.patchValue({
          class_name: el.class_name,
          class_seats: el.class_seats,
          remaining_seats: el.remaining_seats,
          ticket_price: el.ticket_price,
        });

        if (el.hasOwnProperty('class_id')) {
          this.isFirstDelete = true;
        } else {
          this.isFirstDelete = false;
        }
      }

      if (el.class_name === 'business') {
        this.formBusiness.patchValue({
          class_name: el.class_name,
          class_seats: el.class_seats,
          remaining_seats: el.remaining_seats,
          ticket_price: el.ticket_price,
        });

        if (el.hasOwnProperty('class_id')) {
          this.isBusinessDelete = true;
        } else {
          this.isBusinessDelete = false;
        }
      }
    });
  }

}
