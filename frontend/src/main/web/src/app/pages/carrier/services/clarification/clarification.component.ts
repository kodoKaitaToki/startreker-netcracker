import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {clone} from 'ramda';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {MessageService} from 'primeng/components/common/messageservice';
import { HttpResponse } from '@angular/common/http';

import {Service} from '../shared/model/service.model';
import {ServiceService} from '../shared/service/service.service';
import {checkToken} from "../../../../modules/api/index";

@Component({
  selector: 'app-clarification',
  templateUrl: './clarification.component.html',
  styleUrls: ['./clarification.component.scss']
})
export class ClarificationComponent implements OnInit {

  services: Service[] = [];

  status: string = 'UNDER_CLARIFICATION';
  servicesPerPage = 10;
  pageFrom: number = 0;
  serviceNumber: number = 0;

  currentServiceForUpdate: Service;
  form: FormGroup;
  isForUpdateAlertMessage = false;
  filterContent = '';
  page: number = 1;

  constructor(private serviceService: ServiceService,
    private messageService: MessageService) {
  }

  ngOnInit() {
    this.setFormInDefault();
    this.getClarificationServices();
  }

  setFormInDefault() {

    this.form = new FormGroup(
      {
        service_name: new FormControl('', [Validators.required, Validators.minLength(3), Validators.max(50)]),
        service_descr: new FormControl('', [Validators.required, Validators.minLength(3)])
      }
    );

  }

  getClarificationServices(){
    this.serviceService.getServiceByStatus(this.status, this.pageFrom, this.servicesPerPage)
        .subscribe((resp: HttpResponse<any>) => {
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
                          this.getClarificationServices();
                          this.isForUpdateAlertMessage = false;
                        },
                        error => console.log(error)
                      );
  }

  editService(service: Service){
    this.currentServiceForUpdate = service;

    this.form.patchValue({
                          service_name: this.currentServiceForUpdate.service_name,
                          service_descr: this.currentServiceForUpdate.service_descr
                         });
  }

  changeService(service: Service){
    service['service_name'] = this.form.get('service_name').value;
    service['service_descr'] = this.form.get('service_descr').value;
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

  onChange(event: number){
    this.pageFrom = event;
    window.scrollTo(0, 0);
    this.getClarificationServices();
  }

  getServicesAmount(){
    this.serviceService.getServicesAmount(this.status)
                      .subscribe((resp: HttpResponse<any>) => {
                        checkToken(resp.headers);
                        this.serviceNumber = clone(resp.body);
                      });
  }

}
