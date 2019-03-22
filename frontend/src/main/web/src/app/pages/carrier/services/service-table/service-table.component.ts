import {Component, OnInit} from '@angular/core';
import {clone} from 'ramda';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {MessageService} from 'primeng/components/common/messageservice';
import { HttpResponse } from '@angular/common/http';

import {Service} from '../shared/model/service.model';
import {ServiceService} from '../shared/service/service.service';
import { checkToken } from '../../../../modules/api';


@Component({
  selector: 'app-service-table',
  templateUrl: './service-table.component.html',
  styleUrls: ['./service-table.component.scss']
})
export class ServiceTableComponent implements OnInit {

  services: Service[] = [];

  form: FormGroup;
  isForUpdateAlertMessage = false;
  filterContent = '';
  page: number = 1;

  constructor(private serviceService: ServiceService,
    private messageService: MessageService) {
  }

  ngOnInit() {
    this.setFormInDefault();
    this.getApprovedServices();
  }

  setFormInDefault() {

    this.form = new FormGroup(
      {
        service_name: new FormControl('', [Validators.required, Validators.minLength(3), Validators.max(50)]),
        service_descr: new FormControl('', [Validators.required, Validators.minLength(3)])
      }
    );

  }

  getApprovedServices(){
    this.serviceService.getServiceByStatus('PUBLISHED')
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
    if (service.service_status == 'ARCHIVED') {
      createdMessage = this.createMessage('success',
        'The service ' + service.service_name + ' was archieved',
        'You can find it in Archive'
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
                          this.getApprovedServices();
                          this.isForUpdateAlertMessage = false;
                        },
                        error => console.log(error)
                      );
  }
  
  changeService(service: Service){
    service['service_name'] = this.form.get('service_name').value;
    service['service_descr'] = this.form.get('service_descr').value;
    service['service_status'] = 'OPEN';
    this.updateService(service);
  }

  closeUpdateForm(){
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
