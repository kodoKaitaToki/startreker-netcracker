import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-header',
  templateUrl: './app-header.component.html',
  styleUrls: ['./app-header.component.scss']
})
export class AppHeaderComponent implements OnInit {

  btn: boolean = true;

  constructor() { }

  ngOnInit() {this.btn = (localStorage.getItem('rt') === null ? true : false);}

}
