import {Component, Input, OnInit} from '@angular/core';
import {Approver} from '../shared/model/approver';
import {FormControl, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-approver-table',
  templateUrl: './approver-table.component.html',
  styleUrls: ['./approver-table.component.scss']
})
export class ApproverTableComponent implements OnInit {

  @Input() approvers: Approver;

  @Input() filterCriteria: string;
  @Input() filterContent: string;

  currentApproverForUpdate = new Approver();

  form;

  constructor() {

  }

  ngOnInit() {

    this.form = new FormGroup(
      {
        email: new FormControl(this.currentApproverForUpdate.email, [Validators.required, Validators.email]),
        name: new FormControl(this.currentApproverForUpdate.name, Validators.required),
        tel: new FormControl(this.currentApproverForUpdate.telephone, [Validators.required, Validators.pattern('[0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]')]),
        status: new FormControl(this.currentApproverForUpdate.is_activated,)
      }
    );

    this.setRadioButtonDefaultStatus();
  }

  setRadioButtonDefaultStatus() {
    this.form.patchValue({
      status: 'activated'
    });
  }

  onUpdate(event) {

    this.currentApproverForUpdate = event;
    console.log(this.currentApproverForUpdate);
  }

  onDelete() {

  }

  onSubmit() {
    console.log(this.form);
  }

}
