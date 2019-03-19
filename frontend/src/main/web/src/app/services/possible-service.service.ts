import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { clone } from 'ramda';

import { Api, HttpOptions, HttpOptionsAuthorized } from '../modules/api';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PossibleServiceService {

  constructor(private http: HttpClient) { }

  getAll(classId: number): Observable<any> {
    return this.http.get<any>(Api.possibleServices.possibleServices() + '?class-id=' + classId, HttpOptionsAuthorized)
  }
}
