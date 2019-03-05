import { Component, OnInit } from '@angular/core';
import { clone } from 'ramda';
import {FormControl, FormGroup, Validators} from '@angular/forms';

import { Service } from '../service.model';
import { ServiceService } from '../service.service';

@Component({
  selector: 'app-archive',
  templateUrl: './archive.component.html',
  styleUrls: ['./archive.component.scss']
})
export class ArchiveComponent implements OnInit {

  services: Service[] = [];

  currentServiceForUpdate: Service;
  form: FormGroup;
  isForUpdateAlertMessage = false;

  constructor(private serviceService: ServiceService) { }

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
    this.serviceService.getServiceByStatus(6)
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

  deleteService(id){
    this.serviceService.deleteService(id)
                      .subscribe(
                        (resp: Response) => {
                          /*if (resp.headers.get('New-Access-Token')) {
                            localStorage.removeItem('at');
                            localStorage.setItem('at', resp.headers.get('New-Access-Token'));
                          }*/
                          this.getArchievedServices();
                        },
                        error => console.log(error)
                      );
  }

  updateService(service: Service){
    this.isForUpdateAlertMessage = true;
    console.log(service);
    this.serviceService.updateService(service)
                      .subscribe(
                        (resp: Response) => {
                          /*if (resp.headers.get('New-Access-Token')) {
                            localStorage.removeItem('at');
                            localStorage.setItem('at', resp.headers.get('New-Access-Token'));
                          }*/
                          this.getArchievedServices();
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
    this.updateService(service);
  }

  closeUpdateForm(){
    this.currentServiceForUpdate = null;
    this.isForUpdateAlertMessage = false;
  }

}
