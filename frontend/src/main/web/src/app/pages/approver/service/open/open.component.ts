import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';

import { Service } from '../shared/model/service';
import { MOCK_DATA } from '../shared/model/mock-data';
import { ServiceService } from '../shared/service/service.service';

@Component({
  selector: 'app-open',
  templateUrl: './open.component.html',
  styleUrls: ['./open.component.scss']
})
export class OpenComponent implements OnInit {

  @Input() services: Service[];

  loadingService: Service;

  constructor(private serviceService: ServiceService) { }

  resetLoading() {
    this.loadingService = null;
  }

  onAssign(service) {
    service.service_status = 3;

    this.loadingService = service;

    this.serviceService.updateServiceReview(service)
    .subscribe(() => {
      this.getServices();
      alert(service.service_name + ' is now assigned to you');
    }, () => {
      this.resetLoading();
      alert('Something went wrong');
    });
  }

  getServices() {
    this.serviceService.getServicesForApprover(0, 10, 2)
    .subscribe(data => {
      this.resetLoading();
      this.services = data;
    });
  }

  ngOnInit() {
    this.getServices();
  }
}
