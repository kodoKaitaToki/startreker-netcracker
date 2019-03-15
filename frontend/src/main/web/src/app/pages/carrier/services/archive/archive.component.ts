import {Component, OnInit} from '@angular/core';
import {clone} from 'ramda';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {MessageService} from 'primeng/components/common/messageservice';

import {Service} from '../shared/model/service.model';
import {ServiceService} from '../shared/service/service.service';

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
  filterContent = '';
  page: number = 1;

  constructor(private serviceService: ServiceService,
    private messageService: MessageService) {
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
    this.serviceService.getServiceByStatus('ARCHIVED')
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

  updateService(service: Service, message: String) {
    this.isForUpdateAlertMessage = true;
    let createdMessage = '';
    if (message == 'removed') {
      createdMessage = this.createMessage('success',
        'The service ' + service.service_name + ' was removed',
        "You won't see it any more"
      );
    } else if (message == 'edited') {
      createdMessage = this.createMessage('success',
        'The service ' + service.service_name + ' was edited',
        'It was sent for approvement'
      );
    } else {
      createdMessage = this.createMessage('success',
        'The service ' + service.service_name + ' was restored',
        'It was sent for approvement'
      );
    }
    this.serviceService.updateService(service)
                      .subscribe(
                        (resp: Response) => {
                          /*if (resp.headers.get('New-Access-Token')) {
                            localStorage.removeItem('at');
                            localStorage.setItem('at', resp.headers.get('New-Access-Token'));
                          }*/
                          this.showMessage(createdMessage);
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
    this.updateService(service, 'edited');
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
