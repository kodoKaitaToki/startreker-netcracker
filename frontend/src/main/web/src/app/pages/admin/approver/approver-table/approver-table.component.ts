import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Approver} from '../shared/model/approver';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {ApproverService} from "../shared/services/approver.service";

@Component({
             selector: 'app-approver-table',
             templateUrl: './approver-table.component.html',
             styleUrls: ['./approver-table.component.scss']
           })
export class ApproverTableComponent implements OnInit {

  @Input() approvers: Approver[];

  @Input() filterCriteria: string;

  @Input() filterContent: string;

  currentApproverForUpdate: Approver;

  isForUpdateAlertMessage = false;

  isEditButtonBlockedAfterSubmit = true;

  form: FormGroup;

  totalRec: number;

  page: number = 1;

  entriesAmountOnPage = 10;

  @Output() onUpdateDataNotifier = new EventEmitter();

  constructor(private approverSrvc: ApproverService) {

  }

  ngOnInit() {

    this.setFormInDefault();
    this.totalRec = this.approvers.length;
  }

  setFormInDefault() {

    this.form = new FormGroup(
      {
        email: new FormControl('', [Validators.required, Validators.email]),
        username: new FormControl('', Validators.required),
        telephone_number: new FormControl('', Validators.required),
        is_activated: new FormControl(false, Validators.required),
      }
    );
  }

  onApproverUpdate(onClickedApproverForUpdate) {

    this.isEditButtonBlockedAfterSubmit = true;

    this.currentApproverForUpdate = onClickedApproverForUpdate;

    this.form.patchValue({
                           email: this.currentApproverForUpdate.email,
                           username: this.currentApproverForUpdate.username,
                           telephone_number: this.currentApproverForUpdate.telephone_number,
                           is_activated: this.currentApproverForUpdate.is_activated
                         });
  }

  onApproverDelete(onClickedApproverForDelete) {

    this.approverSrvc.deleteApprover((ApproverTableComponent.deleteUnnecessaryFieldAfterClick(onClickedApproverForDelete)))
        .subscribe(
          () => {
            this.onUpdateDataNotifier.emit();
          },
          () => alert('On updating an error occurred'));
  }

  onSubmitUpdate() {

    this.isEditButtonBlockedAfterSubmit = false;
    this.isForUpdateAlertMessage = true;

    this.form.value.id = this.currentApproverForUpdate.id;

    this.approverSrvc.putApprover(this.form.value)
        .subscribe(
          () => {
          },
          () => alert('On updating an error occurred')
        );

    setTimeout(() => {
                 this.closeUpdateForm();

                 this.onUpdateDataNotifier.emit();
               },
               5000);
  }

  onChangePage($event) {

    this.page = $event;
    window.scrollTo(0, 0);
  }

  static deleteUnnecessaryFieldAfterClick(approver): Approver {
    delete approver['roles'];
    delete approver['user_created_date'];

    return approver;
  }

  closeUpdateForm() {
    this.currentApproverForUpdate = null;
    this.isForUpdateAlertMessage = false;
  }
}
