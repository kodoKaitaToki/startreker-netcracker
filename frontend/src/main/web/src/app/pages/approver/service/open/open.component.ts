import {Component, Input, OnInit} from '@angular/core';

import {FormControl, FormGroup, Validators} from '@angular/forms';
import { clone } from 'ramda';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Service } from '../shared/model/service';
import { MOCK_DATA } from '../shared/model/mock-data';
import { ServiceService } from '../shared/service/service.service';
import { checkToken } from '../../../../modules/api';
import { MessageService } from 'primeng/api';

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

  constructor(
    private serviceService: ServiceService,
    private messageService: MessageService) {
  }

  resetLoading() {
    this.loadingService = null;
  }

  onAssign(service) {
    service.service_status = "ASSIGNED";

    this.loadingService = service;

    this.serviceService.updateServiceReview(service)
    .subscribe((resp: HttpResponse<any>) => {
      checkToken(resp.headers);
      this.getServices();
      this.showMessage(this.createMessage('success', `Service was assigned`, `${service.service_name} is now assigned to you`));
    }, (error: HttpErrorResponse) => {
      this.resetLoading();
      this.showMessage(this.createMessage('error', `Error message - ${error.error.status}`, error.error.message));
    });
  }

  getServices() {
    this.serviceService.getServicesForApprover(this.pageFrom, this.pageNumber, "OPEN")
    .subscribe((resp: HttpResponse<any>) => {
      checkToken(resp.headers);
      this.resetLoading();
      this.services = resp.body.map(item => {
        return new Service(
            item.id,
            item.approver_name,
            item.service_name,
            item.service_descr,
            item.service_status,
            new Date(item.creation_date),
            item.reply_text
        );
      });
      this.showMessage(this.createMessage('success', 'service list', 'The list was updated'));
    }, (error: HttpErrorResponse) => {
      this.resetLoading();
      this.showMessage(this.createMessage('error', `Error message - ${error.error.status}`, error.error.message));
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

  createMessage(severity: string, summary: string, detail: string): any {
    return {
      severity: severity,
      summary: summary,
      detail: detail
    };
  }

  showMessage(msgObj: any) {
    this.messageService.add(msgObj);
  }
}
