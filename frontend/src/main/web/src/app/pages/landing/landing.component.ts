import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-landing',
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.scss']
})
export class LandingComponent implements OnInit {

  constructor() { }

  ngOnInit() {
    console.log(localStorage.getItem('at'));
    console.log(localStorage.getItem('rt'));
  }
  
  getUrl()
  {
    return "url('assets/images/bg.jpg')";
  }

}
