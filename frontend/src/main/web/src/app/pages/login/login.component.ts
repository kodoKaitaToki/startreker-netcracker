import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ApiUserService} from '../../services/auth.service';
import {LoginFormData} from '../../services/interfaces/login-form.interface';
import {MessageService} from "primeng/api";
import {ShowMessageService} from '../admin/approver/shared/service/show-message.service';
import {ActivatedRoute, Router} from '@angular/router';
import {LoginLocations} from '../../shared/loginLocations';
import {Subscription} from "rxjs/internal/Subscription";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  providers: [ShowMessageService]
})
export class LoginComponent implements OnInit, OnDestroy {

  loginForm: FormGroup;
  formData: LoginFormData;
  submitted = false;
  submitBut = false;

  usernameMinLength = 3;
  usernameMaxLength = 24;
  passwrodMinLength = 6;
  passwrodMaxLength = 64;

  message: string = '';
  sub: Subscription;

  constructor(
    private apiService: ApiUserService,
    private formBuilder: FormBuilder,
    private messageService: MessageService,
    private showMsgSrvc: ShowMessageService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.loginForm = this.formBuilder.group({
      username: ['', [Validators.required, Validators.minLength(this.usernameMinLength), Validators.maxLength(this.usernameMaxLength)]],
      password: ['', [Validators.required, Validators.minLength(this.passwrodMinLength), Validators.maxLength(this.passwrodMaxLength)]]
    });

    if (this.router.getCurrentNavigation().extras.state !== undefined)
      this.message = this.router.getCurrentNavigation().extras.state.message;
  }

  get f() {
    return this.loginForm.controls;
  }

  onSubmit() {
    this.submitted = true;
    if (this.loginForm.invalid) {
      return;
    }
    this.submitBut = true;
    this.formData = this.loginForm.value;
    this.apiService.loginUser(this.formData)
      .subscribe((userData) => {
          this.apiService.getLoggedUser(userData);
          this.submitBut = false;
          let role = userData.roles[0];
          this.router.navigateByUrl(LoginLocations[role]);
        },
        error => {
          if (error.error.error == 'UNAUTHORIZED') {
            this.showMsgSrvc.showMessage(this.messageService,
              'error',
              'Wrong password or username',
              'Please check entered data');
          }
          this.submitBut = false;
        });
  }

  ngOnInit() {

  }


  ngOnDestroy() {
  }

}
