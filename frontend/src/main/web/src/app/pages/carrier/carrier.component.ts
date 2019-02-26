import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-carrier',
  templateUrl: './carrier.component.html',
  styleUrls: ['./carrier.component.scss']
})
export class CarrierComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

  getUrl()
  {
    return "url('assets/images/bg.jpg')";
  }

}
