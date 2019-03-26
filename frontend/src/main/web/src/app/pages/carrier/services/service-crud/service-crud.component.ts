import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {clone} from 'ramda';
import {MessageService} from 'primeng/components/common/messageservice';
import { HttpResponse } from '@angular/common/http';

import {Service} from '../shared/model/service.model';
import {ServiceService} from '../shared/service/service.service';
import { checkToken } from '../../../../modules/api';

@Component({
  selector: 'app-service-crud',
  templateUrl: './service-crud.component.html',
  styleUrls: ['./service-crud.component.scss']
})
export class ServiceCrudComponent implements OnInit {

  services: Service[] = [];

  filterContent = '';
  page: number = 1;

  form: FormGroup;
  formTable: FormGroup;

  currentServiceForUpdate: Service;
  isForUpdateAlertMessage = false;

  status: String;

  constructor(private serviceService: ServiceService,
    private messageService: MessageService) {
  }

  ngOnInit(): void {
    this.setFormInDefault();
    this.getDraftServices();
  }

  setFormInDefault() {
    this.form = new FormGroup(
      {
        service_name: new FormControl('', [Validators.required, Validators.minLength(3), Validators.max(50)]),
        service_descr: new FormControl('', [Validators.required, Validators.minLength(3)])
      }
    );

    this.formTable = new FormGroup(
      {
        service_name: new FormControl('', [Validators.required, Validators.minLength(3), Validators.max(50)]),
        service_descr: new FormControl('', [Validators.required, Validators.minLength(3)])
      }
    );
  }

  onPost(status: String) {

    const service: Service = this.form.value;
    service['service_status'] = status;
    let createdMessage = '';
    if (status == 'DRAFT') {
      createdMessage = this.createMessage('success',
        'The service ' + service.service_name + ' was created',
        'You can continue to edit the service later'
      );
    } else {
      createdMessage = this.createMessage('success',
        'The service ' + service.service_name + ' was created',
        'It was sent for approvement'
      );
    }
    this.serviceService.addService(service)
                      .subscribe(
                        (resp: HttpResponse<any>) => {
                          checkToken(resp.headers);
                          this.getDraftServices();
                          this.showMessage(createdMessage);
                        },
                        error => console.log(error)
                      );

    this.form.reset();
  }

  getDraftServices() {
    this.serviceService.getServices(0, 100, 'DRAFT')
                      .subscribe(
                        (resp: HttpResponse<any>) => {
                          checkToken(resp.headers);
                          this.services = clone(resp.body);
                        },
                        error => console.log(error)
                      );
  }

  updateService(service: Service){
    this.isForUpdateAlertMessage = true;
    let createdMessage = '';
    if (service.service_status == 'REMOVED') {
      createdMessage = this.createMessage('success',
        'The service ' + service.service_name + ' was removed',
        "You won't see it any more"
      );
    } else {
      createdMessage = this.createMessage('success',
        'The service ' + service.service_name + ' was edited',
        'It was sent for approvement'
      );
    }
    this.serviceService.updateService(service)
                      .subscribe(
                        (resp: HttpResponse<any>) => {
                          checkToken(resp.headers);
                          this.showMessage(createdMessage);
                          this.getDraftServices();
                          this.isForUpdateAlertMessage = false;
                        },
                        error => console.log(error)
                      );
  }

  editService(service: Service){
    this.currentServiceForUpdate = service;

    this.formTable.patchValue({
                          service_name: this.currentServiceForUpdate.service_name,
                          service_descr: this.currentServiceForUpdate.service_descr
                         });
  }

  changeService(service: Service){
    service['service_name'] = this.formTable.get('service_name').value;
    service['service_descr'] = this.formTable.get('service_descr').value;
    service['service_status'] = 'OPEN';
    this.updateService(service);
  }

  closeUpdateForm(){
    this.currentServiceForUpdate = null;
    this.isForUpdateAlertMessage = false;
  }

  showMessage(msgObj: any) {
    this.messageService.add(msgObj);
  }

  createMessage(severity: string, summary: string, detail: string): any {
    return {
      severity: severity,
      summary: summary,
      detail: detail
    };
  }

  onChangePage(event: number) {
    this.page = event;
  }
}
