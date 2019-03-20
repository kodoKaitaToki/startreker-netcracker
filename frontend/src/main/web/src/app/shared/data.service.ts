import { BehaviorSubject, Subject, Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';

@Injectable({ providedIn: 'root' })
export class DataService {

  private formData = new BehaviorSubject<FormGroup>(new FormGroup({}));

  constructor() { }

  sendFormData(formData) {
    this.formData.next(formData);
  }

  getMessage(): Observable<FormGroup> {
    return this.formData.asObservable();
  }
}