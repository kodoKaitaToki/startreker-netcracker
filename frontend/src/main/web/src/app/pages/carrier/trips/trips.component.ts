import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { TripsService } from '../../../services/trips.service';
import { ExisitingTrips } from '../../../services/interfaces/trips.interface';
import { Trip } from './shared/model/trip';
import { clone } from 'ramda';
import { DateValidator } from './trips.helper';
import {ShowMessageService} from "../../admin/approver/shared/service/show-message.service";
import {MessageService} from "primeng/api";
import {HttpErrorResponse, HttpResponse} from "@angular/common/http";
import { checkToken } from 'src/app/modules/api';

@Component({
  selector: 'app-trips',
  templateUrl: './trips.component.html',
  styleUrls: ['./trips.component.scss'],
  providers: [ShowMessageService]
})
export class TripsComponent implements OnInit {

  defaultTrips: Trip[] = [];
  currentTripForUpdate: Trip;

  filterCriteria = [
    { name: 'trip_status'},
    { name: 'departure_planet' },
    { name: 'arrival_planet' },
    { name: 'departure_date'},
    { name: 'arrival_date'}
  ];

  departureSpaceports: any = [];
  arrivalSpaceports: any = [];

  general: any = {};

  showSuccess: boolean = false;
  showError: boolean = false;

  filterContent = '';

  currentFilter = this.filterCriteria[0].name;
  currentFilterPlaceholder = `Search by ${this.currentFilter}`;

  form: FormGroup;
  submitData: any = {};
  currentDateError: boolean = false;
  departureDateError: boolean = false;


  get f() {
    return this.form.controls;
  }

  constructor(
    private tripsApi: TripsService,
    private showMsgSrvc: ShowMessageService,
    private messageService: MessageService,
  ) {
    this.form = new FormGroup(
      {
        departure_planet: new FormControl('', Validators.required),
        departure_spaceport: new FormControl('', Validators.required),
        arrival_planet: new FormControl('', Validators.required),
        arrival_spaceport: new FormControl('', Validators.required),
        departure_date: new FormControl('', [Validators.required, DateValidator.notEarlierThanCurrentDate]),
        arrival_date: new FormControl('', [Validators.required, DateValidator.notEarlierThanCurrentDate]),
        arrival_time: new FormControl('', Validators.required),
        departure_time: new FormControl('', Validators.required),
      },
      // {
      //   validators: Validators.compose([
      //     DateValidator.dateLessThan('arrival_date', 'departure_date', { 'arrival_date': true })])
      // }
    );
  }

  ngOnInit(): void {
    this.tripsApi.setExistingTrips()
        .subscribe((resp: HttpResponse<ExisitingTrips>) => {
          checkToken(resp.headers);
          this.general = clone(resp.body);
        },
        error => console.log(error)
      );
    this.getTrips();
    // this.defaultTrips = this.getDefaultTrips();
  }

  getTrips() {
    this.tripsApi.getAllTrips()
     .subscribe((resp: HttpResponse<Trip>) => {
        checkToken(resp.headers);
        this.defaultTrips = clone(resp.body);
        },
        error => console.log(error)
      );
  }

  choosePlanet(direction) {
    this[`${direction}Spaceports`] = [...this.general.spaceports[`${this.form.value[`${direction}_planet`]}`]];
  }

  chooseNewFilter(chosenFilterName) {

    this.currentFilter = chosenFilterName.value;

    this.currentFilterPlaceholder = `Search by ${this.currentFilter}`;
  }

  // validateDate() {
  //   const startDate = new Date(this.form.value.departure_date);
  //   const endDate = new Date(this.form.value.arrival_date);
  //   const currentDate = new Date();
  //   // + will then compare the dates' millisecond values
  //   if (+startDate < +currentDate) {
  //     this.currentDateError = true;
  //     setTimeout(() => this.currentDateError = false, 3000);
  //     // console.log('yes')
  //   } else if (+startDate > +endDate) {
  //     // console.log('Start date is less than current date');
  //     this.departureDateError = true;
  //     setTimeout(() => this.departureDateError = false, 3000);
  //   }
  // }

  getTripForUpdate(trip) {
    const id = trip.id;
    delete trip.id;
    this.tripsApi.updateTrip(trip, id)
      .subscribe((resp: HttpResponse<any>) => {
        checkToken(resp.headers);
        this.getTrips();
        this.showMsgSrvc.showMessage(this.messageService, 'success', 'Trip editing', 'The trip was edited');
      }, (error: HttpErrorResponse) => {
        this.showMsgSrvc.showMessage(this.messageService, 'error', `Error message - ${error.error.status}`,
                                      error.error.error);
      });
  }

  ticketClassSubmitHandler(ticketClass) {
    this.tripsApi.saveTicketClass(ticketClass)
      .subscribe((resp: HttpResponse<any>) => {
        checkToken(resp.headers);
        this.getTrips();
        this.showMsgSrvc.showMessage(this.messageService, 'success', 'Ticket class editing', 'Ticket class was updated');
      }, (error: HttpErrorResponse) => {
        this.showMsgSrvc.showMessage(this.messageService, 'error', `Error message - ${error.error.status}`,
                                      error.error.error);
      });
  }

  ticketClassDelete(id) {
    this.tripsApi.deleteTicketClass(id)
      .subscribe((resp: HttpResponse<any>) => {
        checkToken(resp.headers);
        this.getTrips();
        this.showMsgSrvc.showMessage(this.messageService, 'success', 'Ticket class deleting', 'Ticket class was deleted');
      }, (error: HttpErrorResponse) => {
        this.showMsgSrvc.showMessage(this.messageService, 'error', `Error message - ${error.error.status}`,
                                      error.error.error);
      });
  }

  onSubmit() {
    // method to send data to server
    // this.validateDate();
    this.submitData = this.form.value;
    this.submitData.departure_date = `${this.submitData.departure_date} ${this.submitData.departure_time}:00`;
    this.submitData.arrival_date = `${this.submitData.arrival_date} ${this.submitData.arrival_time}:00`;
    delete this.submitData.arrival_time;
    delete this.submitData.departure_time;
    // this.submitData.arrival_time = `${this.submitData.arrival_time}:00`;
    // this.submitData.departure_time = `${this.submitData.departure_time}:00`;
    // console.log(this.submitData);
    this.tripsApi.createTrip(this.submitData)
      .subscribe(
        (resp: HttpResponse<any>) => {
          // console.log(resp);
          checkToken(resp.headers);
          this.showSuccess = true;
          setTimeout(() => this.showSuccess = false, 3000);
          this.getTrips();
        },
        error => {
          // console.error(error);
          this.showError = true;
          setTimeout(() => this.showError = false, 3000);
        }
      );
    // submit form if !this.currentDateError && !this.departureDateError
    this.form.reset();
  }
}
