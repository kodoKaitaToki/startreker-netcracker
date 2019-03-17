import { Component, OnInit } from '@angular/core';
import {SearchService} from "../../services/search.service";

@Component({
  selector: 'app-flights',
  templateUrl: './flights.component.html',
  styleUrls: ['./flights.component.scss']
})
export class FlightsComponent implements OnInit {

  constructor(private svc: SearchService) {

  }

  ngOnInit() {
  }



  getUrl()
  {
    return "url('assets/images/bg.jpg')";
  }

}
