import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Approver} from './shared/model/approver';
import { TripsService } from '../../../services/trips.service';
import { ExisitingTrips } from '../../../services/interfaces/trips.interface';
import { clone } from 'ramda';

@Component({
  selector: 'app-trips',
  templateUrl: './trips.component.html',
  styleUrls: ['./trips.component.scss']
})
export class TripsComponent implements OnInit {

  defaultApprovers: Approver[] = [];
  currentApproverForUpdate: Approver;

  filterCriteria = [
    {name: 'id'},
    {name: 'name'},
    {name: 'status'},
  ];

  departureSpaceports: any = [];
  arrivalSpaceports: any = [];

  general: any = {};

  filterContent = '';

  currentFilter = this.filterCriteria[0].name;
  currentFilterPlaceholder = `Search by ${this.currentFilter}`;

  form: FormGroup;
  submitData: any = {};

  constructor(private tripsApi: TripsService) {
    this.form = new FormGroup(
      {
        departure_planet: new FormControl('', [Validators.required]),
        departure_spaceport: new FormControl('', Validators.required),
        arrival_planet: new FormControl('', Validators.required),
        arrival_spaceport: new FormControl('', Validators.required),
        departure_date: new FormControl('', Validators.required),
        arrival_date: new FormControl('', Validators.required),
      }
    );
  }

  ngOnInit(): void {
    this.tripsApi.setExistingTrips()
    .subscribe(
      (resp: ExisitingTrips) => {
          this.general = clone(resp);
      },
      error => console.log(error)
    );
    this.defaultApprovers = this.getDefaultApprovers();
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

  choosePlanet(direction) {
    this[`${direction}Spaceports`] = [...this.general.spaceports[`${this.form.value[`${direction}_planet`]}`]];
  }

  chooseNewFilter(chosenFilterName) {

    this.currentFilter = chosenFilterName.value;

    this.currentFilterPlaceholder = `Search by ${this.currentFilter}`;
  }

  validateDate() {
    const startDate = new Date(this.form.value.departure_date);
    const endDate = new Date(this.form.value.arrival_date);
    const currentDate = new Date();
    // + will then compare the dates' millisecond values
    if (+startDate > +endDate) {
      console.log('Date is invalid');
    } else if (+startDate < +currentDate) {
      console.log('Start date is less than current date');
    }
  }

  onSubmit() {
    // method to send data to server
    console.log(this.form);
    this.validateDate();
    // this.form.reset();
  }

}
