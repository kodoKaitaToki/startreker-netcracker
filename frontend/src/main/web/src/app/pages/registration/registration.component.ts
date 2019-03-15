import { Component, OnInit } from '@angular/core';
import { FormGroup, Validators, FormBuilder } from '@angular/forms';
import { ApiUserService } from '../../services/auth.service';
import { MustMatch } from './registration.helper';
import { RegisterFormData } from '../../services/interfaces/registration-form.interface';
import { RegisterResponse } from '../../services/interfaces/register-response.interface';
import { clone } from 'ramda';
import { MessageService } from "primeng/api";
import {ShowMessageService} from '../admin/approver/shared/service/show-message.service';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss'],
  providers: [ShowMessageService]
})
export class RegistrationComponent implements OnInit {

  registerForm: FormGroup;
  formData: RegisterFormData;
  submitted = false;
  submitBut = false;

  passwrodMinLength = 6;
  passwrodMaxLength = 64;

  constructor(
    private apiService: ApiUserService,
    private formBuilder: FormBuilder,
    private messageService: MessageService,
    private showMsgSrvc: ShowMessageService
    ) {
    
  }

  get f() {
    return this.registerForm.controls;
  }

  createRegisterForm(){
    this.registerForm = this.formBuilder.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(this.passwrodMinLength), Validators.maxLength(this.passwrodMaxLength)]],
      telephone_number: ['', [Validators.required, Validators.pattern("[0-9]{12}")]],
      match_password: ['', Validators.required]
    }, {
      validator: MustMatch('password', 'match_password')
    })
  }

  onSubmit() {
    this.submitted = true;
    if (this.registerForm.invalid) {
      return;
    }
    this.submitBut = true;
    this.formData = this.registerForm.value;
    this.apiService.registerUser(this.formData)
                  .subscribe((userData: RegisterResponse) => {
                  this.showMsgSrvc.showMessage(this.messageService, 
                                              'success', 
                                              'The password was sent', 
                                              'You can find it in your e-mail account');
                   this.apiService.setUserData(clone(userData));
                   this.submitBut = false;
                   this.registerForm.reset();
                  },
                  error => {
                    this.submitBut = false;
                    this.showMsgSrvc.showMessage(this.messageService, 
                      'error', 
                      'Error', 
                      error.error.message);
                  })
    // this.apiService.registerUser(this.formData);
  }

  ngOnInit() {
    this.createRegisterForm();
  }

}
