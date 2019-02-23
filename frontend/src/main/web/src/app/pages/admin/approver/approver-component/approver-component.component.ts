import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Approver} from '../shared/model/approver';
import {ApproverService} from "../shared/services/approver.service";

@Component({
  selector: 'app-approver-component',
  templateUrl: './approver-component.component.html',
  styleUrls: ['./approver-component.component.scss']
})
export class ApproverComponentComponent implements OnInit {

  approvers: Approver[] = [];
  currentApproverForUpdate: Approver;

  filterCriteria = [
    {name: 'id'},
    {name: 'username'},
    {name: 'user_is_activated'},
  ];

  filterContent = '';

  currentFilter = this.filterCriteria[0].name;
  currentFilterPlaceholder = `Search by ${this.currentFilter}`;


  form: FormGroup;

  constructor(private approverSrvc: ApproverService) {

  }

  ngOnInit(): void {

    this.form = new FormGroup(
      {
        user_email: new FormControl('', [Validators.required, Validators.email]),
        username: new FormControl('', Validators.required),
        user_telephone: new FormControl('', [Validators.required]),
        user_is_activated: new FormControl(true, Validators.required)
      }
    );

    this.approverSrvc.getAll()
      .subscribe(data => this.approvers = data);
  }

  chooseNewFilter(chosenFilterName) {

    this.currentFilter = chosenFilterName.value;
    this.currentFilterPlaceholder = `Search by ${this.currentFilter}`;
  }

  onSubmit() {

    console.log(this.form.value);
    this.form.reset();
  }

  onEmittedApprovers($event) {
    this.approvers = $event;
  }

}
