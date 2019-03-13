import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Trip} from '../shared/model/trip';
import {FormControl, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-trips-table',
  templateUrl: './trips-table.component.html',
  styleUrls: ['./trips-table.component.scss']
})
export class TripsTableComponent implements OnInit {

  @Input() trips: Trip[];

  @Input() filterCriteria: string;

  @Input() filterContent: string;

  currentTripForUpdate: Trip;

  isForUpdateAlertMessage = false;

  isEditButtonBlockedAfterSubmit = true;

  form: FormGroup;

  totalRec: number;

  page: number = 1;

  entriesAmountOnPage = 10;

  @Output() onUpdateDataNotifier = new EventEmitter();

  @Output() onDeleteDataNotifier = new EventEmitter();

  constructor() {

  }

  ngOnInit() {

    this.setFormInDefault();
    this.totalRec = this.trips.length;
  }

  setFormInDefault() {

  //   this.form = new FormGroup(
  //     {
  //       email: new FormControl('', [Validators.required, Validators.email]),
  //       username: new FormControl('', Validators.required),
  //       telephone_number: new FormControl('', Validators.required),
  //       is_activated: new FormControl(false, Validators.required),
  //     }
  //   );
  // }

  // onTripUpdate(onClickedTripForUpdate) {

  //   this.isEditButtonBlockedAfterSubmit = true;

  //   this.currentTripForUpdate = onClickedTripForUpdate;

  //   this.form.patchValue({
  //                          email: this.currentApproverForUpdate.email,
  //                          username: this.currentApproverForUpdate.username,
  //                          telephone_number: this.currentApproverForUpdate.telephone_number,
  //                          is_activated: this.currentApproverForUpdate.is_activated
  //                        });
  }

  onTripDelete(onClickedTripForDelete) {

    this.onDeleteDataNotifier.emit((TripsTableComponent.deleteUnnecessaryFieldAfterClick(onClickedTripForDelete)));
  }

  onSubmitUpdate() {

    this.isEditButtonBlockedAfterSubmit = false;
    this.isForUpdateAlertMessage = true;

    this.form.value.id = this.currentTripForUpdate.trip_id;

    this.onUpdateDataNotifier.emit(this.form.value);
    this.closeUpdateForm();
  }

  onChangePage($event) {

    this.page = $event;
    window.scrollTo(0, 0);
  }

  static deleteUnnecessaryFieldAfterClick(trip): Trip {
    // delete approver['roles'];
    // delete approver['user_created_date'];

    return trip;
  }

  closeUpdateForm() {
    this.currentTripForUpdate = null;
    this.isForUpdateAlertMessage = false;
  }
}

