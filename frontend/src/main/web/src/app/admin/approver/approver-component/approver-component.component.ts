import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Approver} from '../shared/model/approver';

@Component({
  selector: 'app-approver-component',
  templateUrl: './approver-component.component.html',
  styleUrls: ['./approver-component.component.scss']
})
export class ApproverComponentComponent implements OnInit {

  defaultApprovers: Approver[] = [];
  currentApproverForUpdate: Approver;

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

    this.defaultApprovers = this.getDefaultApprovers();

    this.form = new FormGroup(
      {
        email: new FormControl('', [Validators.required, Validators.email]),
        name: new FormControl('', Validators.required),
        tel: new FormControl('', [Validators.required, Validators.pattern('[0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]')]),
        status: new FormControl('activated')
      }
    );
  }

  getDefaultApprovers() {
    return [
      {
        id: '1',
        name: 'testName',
        email: 'test@mail.com',
        telephone: '111-22-33',
        status: 'on',
        creation_date: new Date()
      },
      {
        id: '2',
        name: 'testName',
        email: 'test@mail.com',
        telephone: '111-22-33',
        status: 'on',
        creation_date: new Date()
      },
      {
        id: '3',
        name: 'testName',
        email: 'test@mail.com',
        telephone: '111-22-33',
        status: 'on',
        creation_date: new Date()
      },
      {
        id: '4',
        name: 'testName',
        email: 'test@mail.com',
        telephone: '111-22-33',
        status: 'on',
        creation_date: new Date()
      },
      {
        id: '5',
        name: 'testName',
        email: 'test@mail.com',
        telephone: '111-22-33',
        status: 'on',
        creation_date: new Date()
      },
      {
        id: '6',
        name: 'testName',
        email: 'test@mail.com',
        telephone: '111-22-33',
        status: 'on',
        creation_date: new Date()
      },
      {
        id: '7',
        name: 'testName',
        email: 'test@mail.com',
        telephone: '111-22-33',
        status: 'on',
        creation_date: new Date()
      },
      {
        id: '8',
        name: 'anotherName1',
        email: 'test@mail.com',
        telephone: '111-22-33',
        status: 'on',
        creation_date: new Date()
      },
      {
        id: '9',
        name: 'anotherName2',
        email: 'test@mail.com',
        telephone: '111-22-33',
        status: 'off',
        creation_date: new Date()
      },
      {
        id: '10',
        name: 'testName',
        email: 'test@mail.com',
        telephone: '111-22-33',
        status: 'on',
        creation_date: new Date()
      },
      {
        id: '11',
        name: 'testName',
        email: 'test@mail.com',
        telephone: '111-22-33',
        status: 'off',
        creation_date: new Date()
      }
    ];
  }

  chooseNewFilter(chosenFilterName) {

    this.currentFilter = chosenFilterName.value;

    this.currentFilterPlaceholder = `Search by ${this.currentFilter}`;
  }

  processUpdateEvent(event) {

    this.currentApproverForUpdate = event;
  }

  processDeleteNotification() {
  }

  onSubmit() {

    console.log(this.form);
    this.form.reset();
  }

}
