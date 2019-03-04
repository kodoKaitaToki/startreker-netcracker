import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';

import { Service } from '../shared/model/service';
import { MOCK_DATA } from '../shared/model/mock-data';

@Component({
  selector: 'app-assigned',
  templateUrl: './assigned.component.html',
  styleUrls: ['./assigned.component.scss']
})
export class AssignedComponent implements OnInit {

  @Input() services: Service[];

  form: FormGroup;

  currentServiceForReply: Service;

  setFormInDefault() {
    this.form = new FormGroup(
      {
        reply_text: new FormControl('', [Validators.required]),
      }
    );
  }

  onWriteReply(serviceForReply) {
    this.currentServiceForReply = serviceForReply;
  }

  resetReply() {
    this.currentServiceForReply = null;
  }

  constructor() { }

  ngOnInit() {
    this.setFormInDefault();
    this.services = MOCK_DATA;
  }

}
