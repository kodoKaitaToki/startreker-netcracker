import { Component, OnInit } from '@angular/core';
import { FormGroup, Validators, FormBuilder } from '@angular/forms';
import { ApiUserService } from '../../services/auth.service';


@Component({
  selector: 'app-recovery',
  templateUrl: './recovery.component.html',
  styleUrls: ['./recovery.component.scss']
})
export class RecoveryComponent implements OnInit {

  recoveryForm: FormGroup;
  formData: any;
  submitted = false;

  constructor(
    private apiService: ApiUserService,
    private formBuilder: FormBuilder
    ) {
    this.recoveryForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]]
    })
  }

  get f() {
    return this.recoveryForm.controls;
  }

  onSubmit() {
    this.submitted = true;
    if (this.recoveryForm.invalid) {
      return;
    }
    this.formData = this.recoveryForm.value;
    this.apiService.recoverPassword(this.formData);
  }
  ngOnInit() {
  }

}
