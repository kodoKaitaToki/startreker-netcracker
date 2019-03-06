import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Bundles} from '../shared/model/bundles';
import {FormControl, FormGroup, Validators} from '@angular/forms';

@Component({
             selector: 'app-bundles-table',
             templateUrl: './bundles-table.component.html',
             styleUrls: ['./bundles-table.component.scss']
           })
export class BundlesTableComponent implements OnInit {

  @Input() bundles: Bundles[];

  @Input() filterCriteria: string;

  @Input() filterContent: string;

  currentBundlesForUpdate: Bundles;

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
    this.totalRec = this.bundles.length;
  }

  setFormInDefault() {

    this.form = new FormGroup(
      {
        start_date: new FormControl('', Validators.required),
        finish_date: new FormControl('', Validators.required),
        price: new FormControl('', [Validators.required, Validators.min(0)]),
        description: new FormControl(''),
        trips: new FormControl(''),
        services: new FormControl('')
      }
    );
  }

  onBundlesUpdate(onClickedBundlesForUpdate) {

    this.isEditButtonBlockedAfterSubmit = true;

    this.currentBundlesForUpdate = onClickedBundlesForUpdate;

    this.form.patchValue({
                           start_date: this.currentBundlesForUpdate.start_date,
                           finish_date: this.currentBundlesForUpdate.finish_date,
                           price: this.currentBundlesForUpdate.price,
                           description: this.currentBundlesForUpdate.description,
                           trips : this.currentBundlesForUpdate.trips,
                           services : this.currentBundlesForUpdate.services
                         });
  }

  onBundlesDelete(onClickedBundlesForDelete) {

    this.onDeleteDataNotifier.emit((BundlesTableComponent.deleteUnnecessaryFieldAfterClick(onClickedBundlesForDelete)));
  }

  onSubmitUpdate() {

    this.isEditButtonBlockedAfterSubmit = false;
    this.isForUpdateAlertMessage = true;

    this.form.value.id = this.currentBundlesForUpdate.id;

    this.onUpdateDataNotifier.emit(this.form.value);
    this.closeUpdateForm();
  }

  onChangePage($event) {

    this.page = $event;
    window.scrollTo(0, 0);
  }

  static deleteUnnecessaryFieldAfterClick(bundles): Bundles {
    delete bundles['id'];
    delete bundles['start_date'];

    return bundles;
  }

  closeUpdateForm() {
    this.currentBundlesForUpdate = null;
    this.isForUpdateAlertMessage = false;
  }
}
