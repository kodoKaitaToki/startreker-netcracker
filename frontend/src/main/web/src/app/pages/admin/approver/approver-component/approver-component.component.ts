import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Approver} from '../shared/model/approver';
import {ApproverService} from "../shared/service/approver.service";
import {MessageService} from "primeng/api";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
    selector: 'app-approver-component',
    templateUrl: './approver-component.component.html',
    styleUrls: ['./approver-component.component.scss']
  },
)
export class ApproverComponentComponent implements OnInit {

  approvers: Approver[] = [];

  filterCriteria = [
    {name: 'name'},
    {name: 'email'},
    {name: 'status'},
  ];

  filterContent = '';

  currentFilter = this.filterCriteria[0].name;

  currentFilterPlaceholder = `Search by ${this.currentFilter}`;

  form: FormGroup;

  passwordMinLength = 6;

  isProgressBarActivated = false;

  constructor(private approverSrvc: ApproverService, private messageService: MessageService) {
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

    let approver: Approver = this.form.value;

    delete approver['repeat_password'];

    this.isProgressBarActivated = true;

    this.approverSrvc.postApprover(approver)
        .subscribe(() => {
          this.getAllApprovers();
          this.showMessage(this.createMessage('success', 'Approver creation', 'The approver was created'));
          this.isProgressBarActivated = false;
        }, (error: HttpErrorResponse) => {
          this.showMessage(this.createMessage('error', `Error message - ${error.error.status}`, error.error.error));
          this.isProgressBarActivated = false;
        });

    this.form.reset({is_activated: true});
  }

  getApproverForUpdate(approver) {

    this.isProgressBarActivated = true;

    this.approverSrvc.putApprover(approver)
        .subscribe(() => {
          this.getAllApprovers();
          this.showMessage(this.createMessage('success', 'Approver editing', 'The approver was edited'));
          this.isProgressBarActivated = false;
        }, (error: HttpErrorResponse) => {
          this.showMessage(this.createMessage('error', `Error message - ${error.error.status}`, error.error.error));
          this.isProgressBarActivated = false;
        });
  }

  getApproverForDelete(approver) {

    this.isProgressBarActivated = true;

    this.approverSrvc.deleteApprover(approver)
        .subscribe(() => {
            this.getAllApprovers();
            this.showMessage(this.createMessage('success', 'Approver deletion', 'The approver was deleted'));
            this.isProgressBarActivated = false;
          }, (error: HttpErrorResponse) => {
            this.showMessage(this.createMessage('error', `Error message - ${error.error.status}`, error.error.error));
            this.isProgressBarActivated = false
          }
        );
  }

  getAllApprovers() {
    this.isProgressBarActivated = true;

    this.approverSrvc.getAll()
        .subscribe(data => {
          this.showMessage(this.createMessage('success', 'Approvers list', 'The list was updated'));
          this.approvers = data;
          this.isProgressBarActivated = false;
        }, (error: HttpErrorResponse) => {
          this.showMessage(this.createMessage('error', `Error message - ${error.error.status}`, error.error.error));
          this.isProgressBarActivated = false
        });
  }

  checkRepeatedPasswordTheSame() {

    return this.form.get('password').value === this.form.get('repeat_password').value;
  }

  createMessage(severity: string, summary: string, detail: string): any {
    return {
      severity: severity,
      summary: summary,
      detail: detail
    };
  }

  showMessage(msgObj: any) {
    this.messageService.add(msgObj);
  }
}
