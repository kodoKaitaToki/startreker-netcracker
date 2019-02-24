import { Component, OnInit } from '@angular/core';
import * as CanvasJS from '../../canvasjs.min';
import { FormGroup, FormControl } from '@angular/forms';

@Component({
  selector: 'app-sales',
  templateUrl: './sales.component.html',
  styleUrls: ['./sales.component.scss']
})
export class SalesComponent implements OnInit {

  form = new FormGroup({
    dataPeriod: new FormControl('perWeek'),
    fromDate: new FormControl(new Date()),
    toDate: new FormControl(new Date()),
  });

  constructor() { }

  ngOnInit() {
  }

}
