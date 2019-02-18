import { Component, OnInit } from '@angular/core';

declare function setPoint(point): any;

@Component({
  selector: 'app-search-menu',
  templateUrl: './search-menu.component.html',
  styleUrls: ['./search-menu.component.scss']
})
export class SearchMenuComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

  setPoint(point){
    setPoint(point);
  }

  disableDatepicker(){
    let element = <HTMLInputElement> document.getElementById("returnDate");
    element.disabled = true;
  }

  enableDatepicker(){
    let element = <HTMLInputElement> document.getElementById("returnDate");
    element.disabled = false;
  }
}
