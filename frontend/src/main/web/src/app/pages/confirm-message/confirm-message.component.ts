import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpParams } from '@angular/common/http';
import { MessageService } from "primeng/api";

import { ShowMessageService} from '../admin/approver/shared/service/show-message.service';
import { ApiUserService } from '../../services/auth.service';

@Component({
  selector: 'app-confirm-message',
  templateUrl: './confirm-message.component.html',
  styleUrls: ['./confirm-message.component.scss']
})
export class ConfirmMessageComponent implements OnInit {

  checkBtn: boolean = true;

  constructor(private apiUserService: ApiUserService,
              private activatedRoute: ActivatedRoute,
              private messageService: MessageService,
              private showMsgSrvc: ShowMessageService) {}

  ngOnInit() {
    let token: string;
    this.activatedRoute.queryParams.subscribe(data => {
      token = data['token'];
      let params = new HttpParams().set("token", token);
      this.apiUserService.sendConfirmToken(params).subscribe(
        () => this.checkBtn = false),
        error => this.showMsgSrvc.showMessage(this.messageService, 
                                              'error', 
                                              'Error', 
                                              'The confirmation has been rejected. Try again');
    })
  }
}
