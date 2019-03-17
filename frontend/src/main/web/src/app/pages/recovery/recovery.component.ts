import { Component, OnInit } from '@angular/core';
import { FormGroup, Validators, FormBuilder } from '@angular/forms';
import { clone } from 'ramda';
import { MessageService } from "primeng/api";

import { ApiUserService } from '../../services/auth.service';
import { ShowMessageService} from '../admin/approver/shared/service/show-message.service';

@Component({
  selector: 'app-recovery',
  templateUrl: './recovery.component.html',
  styleUrls: ['./recovery.component.scss'],
  providers: [ShowMessageService]
})
export class RecoveryComponent implements OnInit {

  recoveryForm: FormGroup;
  formData: any;
  submitted = false;
  submitBtn = false;

  constructor(
    private apiService: ApiUserService,
    private formBuilder: FormBuilder,
    private messageService: MessageService,
    private showMsgSrvc: ShowMessageService
    ) {
    this.recoveryForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]]
    })
  }

  get f() {
    return this.recoveryForm.controls;
  }

  onSubmit() {
    this.submitBtn = true;
    this.submitted = true;
    if (this.recoveryForm.invalid) {
      return;
    }
    this.formData = this.recoveryForm.value;
    this.apiService.recoverPassword(this.formData)
                    .subscribe(data => {
                      this.apiService.setUserData(clone(data));
                      this.submitBtn = false;
                      this.showMsgSrvc.showMessage(this.messageService, 
                        'success', 
                        'Password is sent', 
                        'You can find your new password in your mail');
                    },
                    error => {
                      this.submitBtn = false;
                      this.showMsgSrvc.showMessage(this.messageService, 
                        'error', 
                        'Error', 
                        'There are no accounts associated with this e-mail');
                    })
  }

  ngOnInit() {
  }

}
