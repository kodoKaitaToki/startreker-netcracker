import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ApiUserService } from '../../services/auth.service';

@Component({
  selector: 'app-confirm-message',
  templateUrl: './confirm-message.component.html',
  styleUrls: ['./confirm-message.component.scss']
})
export class ConfirmMessageComponent implements OnInit {

  constructor(private apiUserService: ApiUserService) {}

  ngOnInit() {
    this.apiUserService.sendConfirmToken();
  }

  getUrl()
  {
    return "url('assets/images/bg.jpg')";
  }
}
