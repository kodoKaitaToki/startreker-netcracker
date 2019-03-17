import {Component, Input, OnInit} from '@angular/core';

import {Service} from '../shared/model/service';
import {ServiceService} from '../shared/service/service.service';

@Component({
  selector: 'app-open',
  templateUrl: './open.component.html',
  styleUrls: ['./open.component.scss']
})
export class OpenComponent implements OnInit {

  readonly pageNumber: number = 10;

  pageFrom: number;

  services: Service[];

  loadingService: Service;

  constructor(private serviceService: ServiceService) { }

  resetLoading() {
    this.loadingService = null;
  }

  onAssign(service) {
    service.service_status = "ASSIGNED";

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
    this.serviceService.getServicesForApprover(this.pageFrom, this.pageNumber, "OPEN")
    .subscribe(data => {
      this.resetLoading();
      this.services = data;
    });
  }

  ngOnInit() {
    this.pageFrom = 0;
    this.getServices();
  }

  onPageUpdate(from: number) {
    this.pageFrom = from;
    this.getServices();
  }
}
