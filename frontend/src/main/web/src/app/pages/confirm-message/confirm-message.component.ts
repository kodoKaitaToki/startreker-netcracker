import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpParams } from '@angular/common/http';

import { ApiUserService } from '../../services/auth.service';

@Component({
  selector: 'app-confirm-message',
  templateUrl: './confirm-message.component.html',
  styleUrls: ['./confirm-message.component.scss']
})
export class ConfirmMessageComponent implements OnInit {

  checkBtn: boolean = true;

  constructor(private apiUserService: ApiUserService,
              private activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    let token: string;
    this.activatedRoute.queryParams.subscribe(data => {
      token = data['token'];
      let params = new HttpParams().set("token", token);
      this.apiUserService.sendConfirmToken(params).subscribe(() => this.checkBtn = false);
    })
  }
}
