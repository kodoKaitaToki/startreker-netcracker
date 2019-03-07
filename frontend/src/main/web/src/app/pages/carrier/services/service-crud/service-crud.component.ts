import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {clone} from 'ramda';
import {Message} from 'primeng/components/common/api';
import {MessageService} from 'primeng/components/common/messageservice'; 

import { Service } from '../service.model';
import { ServiceService } from '../service.service';

@Component({
  selector: 'app-service-crud',
  templateUrl: './service-crud.component.html',
  styleUrls: ['./service-crud.component.scss'],
  providers: [MessageService]
})
export class ServiceCrudComponent implements OnInit {

  services: Service[] = [];
  msgs: Message[] = [];

  filterCriteria = [
    {name: 'id'},
    {name: 'name'},
    {name: 'status'},
  ];

  filterContent = '';

  currentFilter = this.filterCriteria[0].name;

  currentFilterPlaceholder = `Search by ${this.currentFilter}`;

  form: FormGroup;
  formTable: FormGroup;

  currentServiceForUpdate: Service;
  isForUpdateAlertMessage = false;

  status: String;

  constructor(private serviceService: ServiceService,
              private message: Message,
              private messageService: MessageService) {
  }

  showSuccess(){
    this.messageService.add({severity:'success',summary: 'Service Message', detail: ''});
  }

  ngOnInit(): void {
    this.setFormInDefault();
    this.getDarftServices();
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

  chooseNewFilter(chosenFilterName) {

    this.currentFilter = chosenFilterName.value;
    this.currentFilterPlaceholder = `Search by ${this.currentFilter}`;
  }

  onPost(status: String) {

    const service: Service = this.form.value;
    service['service_status'] = status;
    console.log(service);
    this.serviceService.addService(service)
                      .subscribe(
                        (resp: Response) => {
                          /*if (resp.headers.get('New-Access-Token')) {
                            localStorage.removeItem('at');
                            localStorage.setItem('at', resp.headers.get('New-Access-Token'));
                          }*/
                          this.getDarftServices();
                        },
                        error => console.log(error)
                      );

    this.form.reset();
  }

  getDarftServices(){
    this.serviceService.getServiceByStatus('DRAFT')
                      .subscribe(
                        (resp: Response) => {
                          /*if (resp.headers.get('New-Access-Token')) {
                            localStorage.removeItem('at');
                            localStorage.setItem('at', resp.headers.get('New-Access-Token'));
                          }*/
                          this.services = clone(resp);
                        },
                        error => console.log(error)
                      );
  }

  updateService(service: Service){
    this.isForUpdateAlertMessage = true;
    this.serviceService.updateService(service)
                      .subscribe(
                        (resp: Response) => {
                          /*if (resp.headers.get('New-Access-Token')) {
                            localStorage.removeItem('at');
                            localStorage.setItem('at', resp.headers.get('New-Access-Token'));
                          }*/
                          this.getDarftServices();
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

}
