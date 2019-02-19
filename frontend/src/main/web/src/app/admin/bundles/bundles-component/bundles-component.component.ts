import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Bundle} from '../shared/model/bundle';

@Component({
  selector: 'app-bundles-component',
  templateUrl: './bundles-component.component.html',
  styleUrls: ['./bundles-component.component.scss']
})

export class BundlesComponentComponent implements OnInit {

  defaultBundle: Bundle[] = [];
  currentBundleForUpdate: Bundle;

  filterCriteria = [
    {name: 'id'},
    {name: 'bundle_price'},
  ];

  filterContent = '';

  currentFilter = this.filterCriteria[0].name;
  currentFilterPlaceholder = `Search by ${this.currentFilter}`;

  form: FormGroup;

  constructor() {
  }

  ngOnInit(): void {

    this.defaultBundle = this.getDefaultBundle();

    this.form = new FormGroup(
      {
        start_date: new FormControl(new Date()),
        finish_date: new FormControl(new Date()),
        bundle_price: new FormControl(0),
        bundle_description: new FormControl('')
      }
    );
  }

  getDefaultBundle() {
    return [
      {
        id: 1,
        start_date: new Date('01.01.2019'),
        finish_date: new Date('02.01.2019'),
        bundle_price: 100500,
        bundle_description: 'Description for bundle number 1'
      },

      {
        id: 2,
        start_date: new Date('02.01.2019'),
        finish_date: new Date('01.01.2020'),
        bundle_price: 10500,
        bundle_description: 'Description for bundle number 2'
      },

      {
        id: 3,
        start_date: new Date('03.01.2019'),
        finish_date: new Date('01.02.2019'),
        bundle_price: 10050,
        bundle_description: 'Description for bundle number 3'
      },
    ];
  }

  chooseNewFilter(chosenFilterName) {

    this.currentFilter = chosenFilterName.value;

    this.currentFilterPlaceholder = `Search by ${this.currentFilter}`;
  }

  processUpdateEvent(event) {

    this.currentBundleForUpdate = event;
  }

  processDeleteNotification() {
  }

  onSubmit() {

    console.log(this.form);
  }

}
