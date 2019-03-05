import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';

import { Service } from '../shared/model/service';
import { MOCK_DATA } from '../shared/model/mock-data';
import { ServiceService } from '../shared/service/service.service';

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

  onPublish(service) {
    service.service_status = 4;
    this.serviceService.updateServiceReview(service)
    .subscribe(() => {
      this.getServices();
    }, () => {
      alert('Something went wrong');
    });
  }

  onReview(service) {
    service.service_status = 5;
    service.reply_text = this.form.value.reply_text;
    this.serviceService.updateServiceReview(service)
    .subscribe(() => {
      this.getServices();
    }, () => {
      alert('Something went wrong');
    });
  }

  getServices() {
    this.serviceService.getServicesForApprover(0, 10, 3)
    .subscribe(data => {
      this.services = data;
    });
  }

  constructor(private serviceService: ServiceService) { }

  ngOnInit() {
    this.setFormInDefault();
    this.getServices();
  }

}
