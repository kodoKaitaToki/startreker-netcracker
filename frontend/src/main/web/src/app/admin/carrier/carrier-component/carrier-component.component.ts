import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Carrier} from '../carrier';

@Component({
  selector: 'app-carrier-component',
  templateUrl: './carrier-component.component.html',
  styleUrls: ['./carrier-component.component.scss']
})
export class CarrierComponentComponent implements OnInit {

  defaultCarriers: Carrier[] = [];
  currentCarrierForUpdate: Carrier;

  filterCriteria = [
    {name: 'id'},
    {name: 'name'},
    {name: 'status'},
  ];

  filterContent = '';

  currentFilter = this.filterCriteria[0].name;
  currentFilterPlaceholder = `Search by ${this.currentFilter}`;

  form: FormGroup;

  constructor() {
  }

  ngOnInit(): void {

    this.defaultCarriers = this.getDefaultCarriers();

    this.form = new FormGroup(
      {
        email: new FormControl('', [Validators.required, Validators.email]),
        name: new FormControl('', Validators.required),
        tel: new FormControl('', [Validators.required, Validators.pattern('[0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]')]),
        status: new FormControl('activated')
      }
    );
  }

  getDefaultCarriers() {
    return [
      {
        id: '1',
        name: 'testName',
        email: 'test@mail.com',
        telephone: '111-22-33',
        status: 'true',
        creation_date: new Date()
      },
      {
        id: '2',
        name: 'testName',
        email: 'test@mail.com',
        telephone: '111-22-33',
        status: 'true',
        creation_date: new Date()
      },
      {
        id: '3',
        name: 'testName',
        email: 'test@mail.com',
        telephone: '111-22-33',
        status: 'true',
        creation_date: new Date()
      },
      {
        id: '4',
        name: 'testName',
        email: 'test@mail.com',
        telephone: '111-22-33',
        status: 'true',
        creation_date: new Date()
      },
      {
        id: '5',
        name: 'testName',
        email: 'test@mail.com',
        telephone: '111-22-33',
        status: 'true',
        creation_date: new Date()
      },
      {
        id: '6',
        name: 'testName',
        email: 'test@mail.com',
        telephone: '111-22-33',
        status: 'true',
        creation_date: new Date()
      },
      {
        id: '7',
        name: 'testName',
        email: 'test@mail.com',
        telephone: '111-22-33',
        status: 'true',
        creation_date: new Date()
      },
      {
        id: '8',
        name: 'anotherName1',
        email: 'test@mail.com',
        telephone: '111-22-33',
        status: 'true',
        creation_date: new Date()
      },
      {
        id: '9',
        name: 'anotherName2',
        email: 'test@mail.com',
        telephone: '111-22-33',
        status: 'false',
        creation_date: new Date()
      },
      {
        id: '10',
        name: 'testName',
        email: 'test@mail.com',
        telephone: '111-22-33',
        status: 'true',
        creation_date: new Date()
      },
      {
        id: '11',
        name: 'testName',
        email: 'test@mail.com',
        telephone: '111-22-33',
        status: 'true',
        creation_date: new Date()
      }
    ];
  }

  chooseNewFilter(chosenFilterName) {

    this.currentFilter = chosenFilterName.value;

    this.currentFilterPlaceholder = `Search by ${this.currentFilter}`;
  }

  processUpdateEvent(event) {

    this.currentCarrierForUpdate = event;
  }

  processDeleteNotification() {
  }

  onSubmit() {

    console.log(this.form);
  }

}
