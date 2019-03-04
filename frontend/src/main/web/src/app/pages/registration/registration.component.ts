import { Component, OnInit } from '@angular/core';
import { FormGroup, Validators, FormBuilder } from '@angular/forms';
import { ApiUserService } from '../../services/auth.service';
import { MustMatch } from './registration.helper';
import { RegisterFormData } from '../../services/interfaces/registration-form.interface';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {

  registerForm: FormGroup;
  formData: RegisterFormData;
  submitted = false;

  constructor(
    private apiService: ApiUserService,
    private formBuilder: FormBuilder
    ) {
    this.registerForm = this.formBuilder.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      telephone_number: ['', [Validators.required, Validators.pattern("[0-9]{12}")]],
      match_password: ['', Validators.required]
    }, {
      validator: MustMatch('password', 'match_password')
    })
  }

  get f() {
    return this.registerForm.controls;
  }

  onSubmit() {
    this.submitted = true;
    if (this.registerForm.invalid) {
      return;
    }
    this.formData = this.registerForm.value;
    this.apiService.registerUser(this.formData);
  }

  ngOnInit() {
  }

}
