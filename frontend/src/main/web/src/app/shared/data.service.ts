import {BehaviorSubject, Observable} from 'rxjs';
import {Injectable} from '@angular/core';
import {FormGroup} from '@angular/forms';

@Injectable({ providedIn: 'root' })
export class DataService {

  private formData = new BehaviorSubject<any>(new Object);

  constructor() { }

  sendFormData(formData) {
    this.formData.next(formData);
  }

  getMessage(): Observable<any> {
    return this.formData.asObservable();
  }
}
