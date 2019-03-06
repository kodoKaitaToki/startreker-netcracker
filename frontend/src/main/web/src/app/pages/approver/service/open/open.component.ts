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

  constructor(private serviceService: ServiceService) { }

  onAssign(service) {
    service.service_status = 3;
    this.serviceService.updateServiceReview(service)
    .subscribe(() => {
      this.getServices();
    }, () => {
      alert('Something went wrong');
    });
  }

  getServices() {
    this.serviceService.getServicesForApprover(0, 10, 2)
    .subscribe(data => {
      this.services = data;
    });
  }

  ngOnInit() {
    this.getServices();
  }
}
