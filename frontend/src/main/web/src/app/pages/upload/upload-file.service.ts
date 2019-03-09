import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpEvent } from '@angular/common/http';
import {Observable} from "rxjs/internal/Observable";

import { Api } from '../../../app/modules/api/index';
 
@Injectable()
export class UploadFileService {

  private defaultUrl: string = Api.baseUrl;

  private apiVersion: string = 'api/v1/';

  private url: string;
 
  constructor(private http: HttpClient) {

    this.url = this.defaultUrl + this.apiVersion;
  }
 
  pushFileToStorage(file: File): Observable<HttpEvent<{}>> {
    const formdata: FormData = new FormData();
 
    formdata.append('file', file);
 
    const req = new HttpRequest('POST', this.url + "upload", formdata, {
      reportProgress: true,
      responseType: 'text'
    });
 
    return this.http.request(req);
  }
 
  getFiles(): Observable<any> {
    return this.http.get(this.url + "files");
  }
}