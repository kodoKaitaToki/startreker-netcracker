import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Bundle} from '../shared/model/bundle';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {BundlesService} from "../shared/service/bundles.service";

@Component({
             selector: 'app-bundles-table',
             templateUrl: './bundles-table.component.html',
             styleUrls: ['./bundles-table.component.scss']
           })
export class BundlesTableComponent implements OnInit {
  @Input() filterCriteria: string;

  @Input() filterContent: string;

  readonly pageNumber: number = 10;

  pageFrom: number;

  pageAmount: number;

  bundles: Bundle[];

  currentBundlesForUpdate: Bundle;

  isForUpdateAlertMessage = false;

  isEditButtonBlockedAfterSubmit = true;

  form: FormGroup;

  entriesAmountOnPage = 10;

  @Output() onUpdateDataNotifier = new EventEmitter();

  @Output() onDeleteDataNotifier = new EventEmitter();

  @Output() update = new EventEmitter<number>();

  constructor(private bundlesSrvc: BundlesService) {

  }

  ngOnInit() {
    this.pageFrom = 0;

    this.getAllBundles();

    this.setFormInDefault();
  }

  setFormInDefault() {

    this.form = new FormGroup(
      {
        start_date: new FormControl('', Validators.required),
        finish_date: new FormControl('', Validators.required),
        price: new FormControl('', [Validators.required, Validators.min(0)]),
        description: new FormControl(''),
        trips: new FormControl(''),
      }
    );
  }

  onBundlesUpdate(onClickedBundlesForUpdate) {

    this.isEditButtonBlockedAfterSubmit = true;

    this.currentBundlesForUpdate = onClickedBundlesForUpdate;

    this.form.patchValue({
                           start_date: this.currentBundlesForUpdate.start_date,
                           finish_date: this.currentBundlesForUpdate.finish_date,
                           price: this.currentBundlesForUpdate.bundle_price,
                           description: this.currentBundlesForUpdate.bundle_description,
                           trips : this.currentBundlesForUpdate.bundle_trips,
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

  static deleteUnnecessaryFieldAfterClick(bundles): Bundle {
    delete bundles['id'];
    delete bundles['start_date'];

    return bundles;
  }

  closeUpdateForm() {
    this.currentBundlesForUpdate = null;
    this.isForUpdateAlertMessage = false;
  }

  getBundlesForUpdate(bundles) {

    this.bundlesSrvc.putBundles(bundles)
        .subscribe(() => {
          this.getAllBundles();
        }, () => {
          alert('Something went wrong');
        });
  }

  getBundlesForDelete(bundles) {

    this.bundlesSrvc.deleteBundles(bundles)
        .subscribe(() => {
          this.getAllBundles();
        });
  }

  getAllBundles() {

    this.bundlesSrvc.getCount()
        .subscribe(data => this.pageAmount = Math.floor(data/this.pageNumber));

    this.bundlesSrvc.getBundlesInInterval(this.pageNumber, this.pageFrom)
        .subscribe(data => this.bundles = data);
  }

/*  checkRepeatedPasswordTheSame() {
    return this.form.get('password').value === this.form.get('repeat_password').value;
  } */

  onPageUpdate(from: number) {
    this.pageFrom = from;
    this.getAllBundles();
  }
}
