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
        status: new FormControl('activated')
      }
    );
  }

  onUpdate(event) {

    this.currentApproverForUpdate = event;
    this.isEditButtonBlockedAfterSubmit = true;

    const radioButtonStatus = this.currentApproverForUpdate.is_activated === 'true'? 'activated' : 'deactivated';

    this.form = new FormGroup(
      {
        email: new FormControl(this.currentApproverForUpdate.email, [Validators.required, Validators.email]),
        name: new FormControl(this.currentApproverForUpdate.name, Validators.required),
        tel: new FormControl(this.currentApproverForUpdate.telephone, [Validators.required, Validators.pattern('[0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]')]),
        status: new FormControl(radioButtonStatus)
      }
    );
  }

  onDelete() {

  }

  onSubmit() {

    this.isForUpdateMessage = true;
    this.isEditButtonBlockedAfterSubmit = false;

    setTimeout(() => {

      this.isForUpdateMessage = false;
      this.currentApproverForUpdate = new Approver();

    }, 5000);
  }
}
