import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import {Carrier} from '../carrier';
import {FormControl, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-carrier-table',
  templateUrl: './carrier-table.component.html',
  styleUrls: ['./carrier-table.component.scss']
})
export class CarrierTableComponent implements OnInit {

  @Input() carriers: Carrier;

  @Input() filterCriteria: string;
  @Input() filterContent: string;

  currentCarrierForUpdate = new Carrier();
  isForUpdateMessage = false;

  form: FormGroup;
  isEditButtonBlockedAfterSubmit = true;

  constructor() {

  }

  ngOnInit() {

    this.setFormInDefault();
  }

  setFormInDefault() {

    this.form = new FormGroup(
      {
        email: new FormControl('', [Validators.required, Validators.email]),
        name: new FormControl('', Validators.required),
        tel: new FormControl('', [Validators.required, Validators.pattern('[0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]')]),
        status: new FormControl('on')
      }
    );
  }

  onUpdate(event) {

    this.currentCarrierForUpdate = event;
    this.isEditButtonBlockedAfterSubmit = true;

    this.form = new FormGroup(
      {
        email: new FormControl(this.currentCarrierForUpdate.email, [Validators.required, Validators.email]),
        name: new FormControl(this.currentCarrierForUpdate.name, Validators.required),
        tel: new FormControl(this.currentCarrierForUpdate.telephone, [Validators.required, Validators.pattern('[0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]')]),
        status: new FormControl(this.currentCarrierForUpdate.status)
      }
    );
  }

  onDelete() {

  }

  onSubmit() {
    console.log(this.form);

    this.isForUpdateMessage = true;
    this.isEditButtonBlockedAfterSubmit = false;

    setTimeout(() => {

      this.isForUpdateMessage = false;
      this.currentCarrierForUpdate = new Carrier();

    }, 5000);
  }

}
