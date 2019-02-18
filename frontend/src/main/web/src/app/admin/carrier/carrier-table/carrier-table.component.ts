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

    const radioButtonStatus = this.currentCarrierForUpdate.is_activated === 'true'? 'activated' : 'deactivated';

    this.form = new FormGroup(
      {
        email: new FormControl('', [Validators.required, Validators.email]),
        name: new FormControl('', Validators.required),
        tel: new FormControl('', [Validators.required, Validators.pattern('[0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]')]),
        status: new FormControl(radioButtonStatus)
      }
    );
  }

  onUpdate(event) {

    this.currentCarrierForUpdate = event;
    this.setFormInDefault();
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
