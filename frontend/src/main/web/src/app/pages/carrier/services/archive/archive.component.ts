import {Component, OnInit} from '@angular/core';
import {clone} from 'ramda';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import { MessageService } from "primeng/api";
import { HttpResponse } from '@angular/common/http';

import {Service} from '../shared/model/service.model';
import {ServiceService} from '../shared/service/service.service';
import {checkToken} from "../../../../modules/api/index";
import { ShowMessageService } from '../../../admin/approver/shared/service/show-message.service';

@Component({
  selector: 'app-archive',
  templateUrl: './archive.component.html',
  styleUrls: ['./archive.component.scss'],
  providers: [ShowMessageService]
})
export class ArchiveComponent implements OnInit {

  status: string = 'ARCHIVED';

  services: Service[] = [];
  servicesPerPage = 10;

  currentServiceForUpdate: Service;
  form: FormGroup;
  isForUpdateAlertMessage = false;
  filterContent = '';

  pageFrom: number = 0;
  serviceNumber: number = 0;

  constructor(private serviceService: ServiceService,
              private messageService: MessageService,
              private showMsgSrvc: ShowMessageService) {
  }

  ngOnInit() {
    this.setFormInDefault();
    this.getArchievedServices();
  }

  setFormInDefault() {

    this.form = new FormGroup(
      {
        service_name: new FormControl('', [Validators.required, Validators.minLength(3), Validators.max(50)]),
        service_descr: new FormControl('', [Validators.required, Validators.minLength(3)])
      }
    );

  }

  getArchievedServices(){
    this.serviceService.getServiceByStatus(this.status, this.pageFrom, this.servicesPerPage)
        .subscribe((resp: HttpResponse<any>) => {
                      checkToken(resp.headers);
                      this.services = clone(resp.body);
                    },
                      error => console.log(error)
                  );
  }

  updateService(service: Service, message: String) {
    this.isForUpdateAlertMessage = true;
    this.serviceService.updateService(service)
                      .subscribe(
                        (resp: HttpResponse<any>) => {
                          checkToken(resp.headers);
                          this.showMsgSrvc.showMessage(this.messageService, 
                                                        'success',
                                                        'The service was updated',
                                                        'It was sent for approvement');
                          this.getArchievedServices();
                          this.isForUpdateAlertMessage = false;
                        },
                        error => this.showMsgSrvc.showMessage(this.messageService, 
                                                              'error',
                                                              'Error', 
                                                              error.error.message));
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
    this.updateService(service, 'edited');
  }

  closeUpdateForm(){
    this.currentServiceForUpdate = null;
    this.isForUpdateAlertMessage = false;
  }

  createMessage(severity: string, summary: string, detail: string): any {
    return {
      severity: severity,
      summary: summary,
      detail: detail
    };
  }

  onChange(event: number){
    this.pageFrom = event;
    window.scrollTo(0, 0);
    this.getArchievedServices();
  }

  getServicesAmount(){
    this.serviceService.getServicesAmount(this.status)
                      .subscribe((resp: HttpResponse<any>) => {
                        checkToken(resp.headers);
                        this.serviceNumber = clone(resp.body);
                      });
  }
}
