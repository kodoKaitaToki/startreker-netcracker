import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-approver',
  templateUrl: './approver.component.html',
  styleUrls: ['./approver.component.scss']
})
export class ApproverComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

  getUrl()
  {
    return "url('assets/images/bg.jpg')";
  }

}
