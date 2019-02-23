import {Component, Input, OnInit} from '@angular/core';
import {Bundle} from '../shared/model/bundle';
import {FormControl, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-bundles-table',
  templateUrl: './bundles-table.component.html',
  styleUrls: ['./bundles-table.component.scss']
})
export class BundlesTableComponent implements OnInit {

  @Input() bundles: Bundle;

  @Input() filterCriteria: string;
  @Input() filterContent: string;

  currentBundlesForUpdate = new Bundle();
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
        start_date: new FormControl('', [Validators.required, Validators.max(Number(this.currentBundlesForUpdate.finish_date))]),
        finish_date: new FormControl('', [Validators.required, Validators.max(Number(this.currentBundlesForUpdate.start_date))]),
        bundle_price: new FormControl('',Validators.min(0)),
        bundle_description: new FormControl('')
      }
    );
  }

  onUpdate(event) {

    this.currentBundlesForUpdate = event;
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
      this.currentBundlesForUpdate = new Bundle();

    }, 5000);
  }

}
