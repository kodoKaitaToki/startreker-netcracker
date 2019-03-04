import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';

import{ Service } from '../service.model'

@Component({
  selector: 'app-service-crud',
  templateUrl: './service-crud.component.html',
  styleUrls: ['./service-crud.component.scss']
})
export class ServiceCrudComponent implements OnInit {

  //approvers: Approver[] = [];

  filterCriteria = [
    {name: 'id'},
    {name: 'name'},
    {name: 'status'},
  ];

  filterContent = '';

  currentFilter = this.filterCriteria[0].name;

  currentFilterPlaceholder = `Search by ${this.currentFilter}`;

  form: FormGroup;

  status: Number;

  /*constructor(private approverSrvc: ApproverService) {
  }*/

  ngOnInit(): void {

    this.form = new FormGroup(
      {
        service_name: new FormControl('', [Validators.required, Validators.minLength(3), Validators.max(50)]),
        service_descr: new FormControl('', [Validators.required, Validators.minLength(3)])
      }
    );

    console.log(this.status);

    //this.getAllApprovers();
  }

  chooseNewFilter(chosenFilterName) {

    this.currentFilter = chosenFilterName.value;
    this.currentFilterPlaceholder = `Search by ${this.currentFilter}`;
  }

  onPost(status: Number) {

    const service: Service = this.form.value;
    service['status'] = status;

    /*this.approverSrvc.postApprover(approver)
        .subscribe(() => {
          this.getAllApprovers();
        }, () => {
          alert('Such an email exists');
        });*/

    this.form.reset();
  }

  /*getApproverForUpdate(approver) {

    this.approverSrvc.putApprover(approver)
        .subscribe(() => {
          this.getAllApprovers();
        }, () => {
          alert('Such an email exists');
        });
  }

  getApproverForDelete(approver) {

    this.approverSrvc.deleteApprover(approver)
        .subscribe(() => {
          this.getAllApprovers();
        });
  }

  getAllApprovers() {

    this.approverSrvc.getAll()
        .subscribe(data => this.approvers = data);
  }*/

}
