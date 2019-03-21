import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Trip} from '../shared/model/trip';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import { DateValidator } from '../trips.helper';

@Component({
  selector: 'app-trips-table',
  templateUrl: './trips-table.component.html',
  styleUrls: ['./trips-table.component.scss']
})
export class TripsTableComponent implements OnInit {

  @Input() trips: Trip[];

  @Input() filterCriteria: string;

  @Input() filterContent: string;

  @Input() exisitngDirections: any;

  currentTripForUpdate: Trip;
  currentTicketClassForUpdate: any;

  isForUpdateAlertMessage = false;

  isEditButtonBlockedAfterSubmit = true;

  form: FormGroup;

  totalRec: number;

  page: number = 1;

  entriesAmountOnPage = 10;

  @Output() onUpdateDataNotifier = new EventEmitter();

  @Output() onDeleteDataNotifier = new EventEmitter();

  @Output() onSubmitTicketClassEmitter$ = new EventEmitter();

  @Output() deleteTicketClassEmitter$ = new EventEmitter();

  @Output() deleteTripEmitter$ = new EventEmitter();

  @Output() approvementTripEmitter$ = new EventEmitter();

  @Output() archiveTripEmitter$ = new EventEmitter();

  @Output() restoreTripEmitter$ = new EventEmitter();

  showTicketClass: boolean = false;

  constructor() {

  }

  ngOnInit() {

    this.setFormInDefault();
    this.totalRec = this.trips.length;
  }

  isUneditAble(trip_status_id) {
    const uneditableIds = [3, 4, 5];

    return uneditableIds.find(el => el === trip_status_id);
  }

  toggleShowTicketClass(ticketClassforUpdate) {
    this.showTicketClass = !this.showTicketClass;
    this.currentTicketClassForUpdate = ticketClassforUpdate;
  }

  setFormInDefault() {
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
      }
    );
  }

  onTripUpdate(onClickedTripForUpdate) {

    this.isEditButtonBlockedAfterSubmit = true;

    this.currentTripForUpdate = onClickedTripForUpdate;

    const departure_date_time = this.currentTripForUpdate.departure_date.split(' ');
    const arrival_date_time = this.currentTripForUpdate.arrival_date.split(' ');

    this.form.patchValue({
      departure_planet: this.currentTripForUpdate.departure_planet,
      departure_spaceport: this.currentTripForUpdate.departure_spaceport,
      arrival_planet: this.currentTripForUpdate.arrival_planet,
      arrival_spaceport: this.currentTripForUpdate.arrival_spaceport,
      departure_date: departure_date_time[0],
      arrival_date: arrival_date_time[0],
      arrival_time: arrival_date_time[1],
      departure_time: departure_date_time[1],
    });
  }

  onTicketClassUpdate(onClickedTicketClassForUpdate) {

    this.isEditButtonBlockedAfterSubmit = true;

    this.currentTicketClassForUpdate = onClickedTicketClassForUpdate;

    const departure_date_time = this.currentTripForUpdate.departure_date.split(' ');
    const arrival_date_time = this.currentTripForUpdate.arrival_date.split(' ');

    this.form.patchValue({
      departure_planet: this.currentTripForUpdate.departure_planet,
      departure_spaceport: this.currentTripForUpdate.departure_spaceport,
      arrival_planet: this.currentTripForUpdate.arrival_planet,
      arrival_spaceport: this.currentTripForUpdate.arrival_spaceport,
      departure_date: departure_date_time[0],
      arrival_date: arrival_date_time[0],
      arrival_time: arrival_date_time[1],
      departure_time: departure_date_time[1],
    });
  }

  onTripDelete(onClickedTripForDelete) {
     this.deleteTripEmitter$.emit(onClickedTripForDelete);
  }

  onTripSendForApprovement(onClickedTripForApprovement) {
    this.approvementTripEmitter$.emit(onClickedTripForApprovement);
  }
  
  onTripArchive(onClickedTripForArchive) {
    this.archiveTripEmitter$.emit(onClickedTripForArchive);
  }
  onTripRestore(onClickedTripForRestore) {
    this.restoreTripEmitter$.emit(onClickedTripForRestore);
  }

  onSubmitUpdate() {

    this.isEditButtonBlockedAfterSubmit = false;
    this.isForUpdateAlertMessage = true;

    this.form.value.id = this.currentTripForUpdate.trip_id;

    const checkDep = this.form.value.departure_time.split(':');
    const checkArr = this.form.value.arrival_time.split(':');

    this.form.value.departure_date = `${this.form.value.departure_date} ${this.form.value.departure_time}`;
    this.form.value.arrival_date = `${this.form.value.arrival_date} ${this.form.value.arrival_time}`;

    if(checkDep.length <= 2) {
      this.form.value.departure_date = `${this.form.value.departure_date}:00`;
    }
    if(checkArr.length <= 2) {
      this.form.value.arrival_date = `${this.form.value.arrival_date}:00`;
    }

    delete this.form.value.arrival_time;
    delete this.form.value.departure_time;

    this.onUpdateDataNotifier.emit(this.form.value);
    this.closeUpdateForm();
  }

  onChangePage($event) {

    this.page = $event;
    window.scrollTo(0, 0);
  }

  updateTickeClassNotifier($event) {
    this.onSubmitTicketClassEmitter$.emit($event);
    this.showTicketClass = !this.showTicketClass;
  }

  deleteTicketClass($event) {
    this.deleteTicketClassEmitter$.emit($event);
    this.showTicketClass = !this.showTicketClass;
  }

  closeUpdateForm() {
    this.currentTripForUpdate = null;
    this.isForUpdateAlertMessage = false;
  }
}

