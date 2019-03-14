import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Approver} from '../shared/model/approver';
import {ApproverService} from "../shared/service/approver.service";
import {MessageService} from "primeng/api";
import {HttpErrorResponse} from "@angular/common/http";
import {ShowMessageService} from "../shared/service/show-message.service";
import { clone } from 'ramda';
import {checkToken} from "../../../../modules/api/index";

@Component({
             selector: 'app-approver-component',
             templateUrl: './approver-component.component.html',
             styleUrls: ['./approver-component.component.scss'],
             providers: [ShowMessageService]
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

  constructor(private approverSrvc: ApproverService,
              private messageService: MessageService,
              private showMsgSrvc: ShowMessageService) {
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

    this.approverSrvc.postApprover(approver)
        .subscribe((resp: Response) => {
          checkToken(resp.headers);
          this.getAllApprovers();
          this.showMsgSrvc.showMessage(this.messageService, 'success', 'Approver creation', 'The approver was created');
        }, (error: HttpErrorResponse) => {
          this.showMsgSrvc.showMessage(this.messageService, 'error', `Error message - ${error.error.status}`,
                                       error.error.error);
        });

    this.form.reset({is_activated: true});
  }

  getApproverForUpdate(approver) {

    this.approverSrvc.putApprover(approver)
        .subscribe((resp: Response) => {
          checkToken(resp.headers);
          this.getAllApprovers();
          this.showMsgSrvc.showMessage(this.messageService, 'success', 'Approver editing', 'The approver was edited');
        }, (error: HttpErrorResponse) => {
          this.showMsgSrvc.showMessage(this.messageService, 'error', `Error message - ${error.error.status}`,
                                       error.error.error);
        });
  }

  getApproverForDelete(approver) {

    this.approverSrvc.deleteApprover(approver)
        .subscribe((resp: Response) => {
                     checkToken(resp.headers);
                     this.getAllApprovers();
                     this.showMsgSrvc.showMessage(this.messageService, 'success', 'Approver deletion', 'The approver was deleted');
                   }, (error: HttpErrorResponse) => {
                     this.showMsgSrvc.showMessage(this.messageService, 'error', `Error message - ${error.error.status}`,
                                                  error.error.error);
                   }
        );
  }

  getAllApprovers() {

    this.approverSrvc.getAll()
        .subscribe((resp: Response) => {
          checkToken(resp.headers);
          this.showMsgSrvc.showMessage(this.messageService, 'success', 'Approvers list', 'The list was updated');
          this.approvers = clone(resp);
        }, (error: HttpErrorResponse) => {
          this.showMsgSrvc.showMessage(this.messageService, 'error', `Error message - ${error.error.status}`,
                                       error.error.error);
        });
  }

  checkRepeatedPasswordTheSame() {

    return this.form.get('password').value === this.form.get('repeat_password').value;
  }
}
