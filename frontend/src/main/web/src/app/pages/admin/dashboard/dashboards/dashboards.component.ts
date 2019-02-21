import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-dashboards',
  templateUrl: './dashboards.component.html',
  styleUrls: ['./dashboards.component.scss']
})
export class DashboardsComponent implements OnInit {

  defaultCarriers = [];
  carriers = [];

  constructor() { }

  ngOnInit() {
    this.defaultCarriers = this.getDefaultCarriers();
  }

  getDefaultCarriers(){

    return [
      {
        id: '1',
        weak: [50,30,10],
        month: [40,20,8]
      }
    ];
  }

}
