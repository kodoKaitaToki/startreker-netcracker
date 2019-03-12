import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';

import { Trip } from '../trip.model';

@Component({
  selector: 'app-assigned',
  templateUrl: './assigned.component.html',
  styleUrls: ['./assigned.component.scss']
})
export class AssignedTripComponent implements OnInit {

  moreFlag: Boolean = false;

  form: FormGroup;

  currentTripForReply: Trip;

  constructor() { }

  ngOnInit() {
  }

  setFormInDefault() {
    this.form = new FormGroup(
      {
        reply_text: new FormControl('', [Validators.required]),
      }
    );
  }

  onWriteReply(tripForReply: Trip) {
    this.currentTripForReply = tripForReply;
  }

  resetReply() {
    this.currentTripForReply = null;
  }

  resetLoading() {
    //this.loadingService = null;
  }

  onPublish(){

  }

}
