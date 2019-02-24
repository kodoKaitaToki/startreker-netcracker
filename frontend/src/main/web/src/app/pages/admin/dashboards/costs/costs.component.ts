import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-costs',
  templateUrl: './costs.component.html',
  styleUrls: ['./costs.component.scss']
})
export class CostsComponent implements OnInit {

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
