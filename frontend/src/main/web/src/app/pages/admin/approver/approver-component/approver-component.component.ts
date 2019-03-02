import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Approver} from '../shared/model/approver';
import {ApproverService} from "../shared/service/approver.service";

@Component({
             selector: 'app-approver-component',
             templateUrl: './approver-component.component.html',
             styleUrls: ['./approver-component.component.scss']
           },
)
export class ApproverComponentComponent implements OnInit {

  approvers: Approver[] = [];

  filterCriteria = [
    {name: 'id'},
    {name: 'name'},
    {name: 'status'},
  ];

  filterContent = '';

  currentFilter = this.filterCriteria[0].name;

  currentFilterPlaceholder = `Search by ${this.currentFilter}`;

  form: FormGroup;

  passwordMinLength = 6;

  constructor(private approverSrvc: ApproverService) {
  }

  ngOnInit(): void {

    this.form = new FormGroup(
      {
        email: new FormControl('', [Validators.required, Validators.email]),
        username: new FormControl('', [Validators.required, Validators.minLength(3), Validators.max(24)]),
        password: new FormControl('', [Validators.required, Validators.minLength(this.passwordMinLength)]),
        repeat_password: new FormControl('', Validators.required),
        telephone_number: new FormControl('', [Validators.required, Validators.pattern('[\\s\\d+(d+)-\\s]+')]),
        is_activated: new FormControl(true, Validators.required)
      }
    );

    this.getAllApprovers();
  }

  chooseNewFilter(chosenFilterName) {

    this.currentFilter = chosenFilterName.value;
    this.currentFilterPlaceholder = `Search by ${this.currentFilter}`;
  }

  onPost() {

    const approver: Approver = this.form.value;

    this.approverSrvc.postApprover(approver)
        .subscribe(() => {
          this.getAllApprovers();
        }, () => {
          alert('Something went wrong');
        });

    this.form.reset({is_activated: true});
  }

  getApproverForUpdate(approver) {

    this.approverSrvc.putApprover(approver)
        .subscribe(() => {
          this.getAllApprovers();
        }, () => {
          alert('Something went wrong');
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
  }

  checkRepeatedPasswordTheSame() {

    return this.form.get('password').value === this.form.get('repeat_password').value;
  }
}
