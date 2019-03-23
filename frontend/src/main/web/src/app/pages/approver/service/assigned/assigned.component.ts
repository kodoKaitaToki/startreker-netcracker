import {Component, Input, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import { clone } from 'ramda';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import {Service} from '../shared/model/service';
import { ServiceService } from '../shared/service/service.service';
import { checkToken } from '../../../../modules/api';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-assigned',
  templateUrl: './assigned.component.html',
  styleUrls: ['./assigned.component.scss']
})
export class AssignedComponent implements OnInit {

  readonly pageNumber: number = 10;

  pageFrom: number;

  services: Service[];

  form: FormGroup;

  currentServiceForReply: Service;

  loadingService: Service;

  constructor(
    private serviceService: ServiceService,
    private messageService: MessageService) {
  }

  
  ngOnInit() {
    this.pageFrom = 0;
    this.setFormInDefault();
    this.getServices();
  }

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

  resetLoading() {
    this.loadingService = null;
  }

  onPublish(service) {
    service.service_status = "PUBLISHED";

    this.loadingService = service;

    this.serviceService.updateServiceReview(service)
    .subscribe((resp: HttpResponse<any>) => {
      checkToken(resp.headers);
      this.getServices();
      this.showMessage(this.createMessage('success', `Service was reviewed`, `${service.service_name} is now published`));
    }, (error: HttpErrorResponse) => {
      this.resetLoading();
      this.showMessage(this.createMessage('error', `Error message - ${error.error.status}`, error.error.message));
    });
  }

  onReview(service) {
    service.service_status = "UNDER_CLARIFICATION";
    service.reply_text = this.form.value.reply_text;

    this.loadingService = service;

    this.serviceService.updateServiceReview(service)
    .subscribe((resp: HttpResponse<any>) => {
      checkToken(resp.headers);
      this.getServices();
      this.showMessage(this.createMessage('success', `Service was reviewed`, `${service.service_name} is now under clarification`));
    }, (error: HttpErrorResponse) => {
      this.resetLoading();
      this.showMessage(this.createMessage('error', `Error message - ${error.error.status}`, error.error.message));
    });
  }

  getServices() {
    this.serviceService.getServicesForApprover(this.pageFrom, this.pageNumber, "ASSIGNED")
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
