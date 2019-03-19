import { Component, OnInit } from '@angular/core';
import * as CanvasJS from '../../canvasjs.min';

import { FormGroup, FormControl } from '@angular/forms';
import {formatDate} from '@angular/common';
import { clone } from 'ramda';

import { DashboardDeltaService } from '../dashboard-delta.service';
import {checkToken} from "../../../../modules/api/index";
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'app-dashboard-delta',
  templateUrl: './dashboard-delta.component.html',
  styleUrls: ['./dashboard-delta.component.scss']
})
export class DashboardDeltaComponent implements OnInit {

  userMap: Map<Date, number> = new Map();
  userChart: CanvasJS.Chart;

  carrierMap: Map<Date, number> = new Map();
  carrierChart: CanvasJS.Chart;

  locationsMap: Map<Date, number> = new Map();
  locationsChart: CanvasJS.Chart;

  form = new FormGroup({
    fromDate: new FormControl(new Date().setDate(1)),
    toDate: new FormControl(new Date()),
  });

  constructor(private dashboardDeltaService: DashboardDeltaService) { }

  onSubmit() {
    let fromDateFormatted: string = formatDate(this.form.value.fromDate, 'yyyy-MM-dd', 'en');
    let toDateFormatted: string = formatDate(this.form.value.toDate, 'yyyy-MM-dd', 'en');

    this.dashboardDeltaService.getUsersIncreasingPerPeriod(fromDateFormatted, toDateFormatted)
      .subscribe((resp: HttpResponse<any>) => {
        checkToken(resp.headers);
        let body = resp.body
        this.userMap = this.convertBodyToMap(resp);
        this.reloadCharts();
    });

    this.dashboardDeltaService.getCarriersIncreasingPerPeriod(fromDateFormatted, toDateFormatted)
      .subscribe((resp: HttpResponse<any>) => {
        checkToken(resp.headers);
        this.carrierMap = this.convertBodyToMap(resp);
        this.reloadCharts();
    });

    this.dashboardDeltaService.getLocationsIncreasingPerPeriod(fromDateFormatted, toDateFormatted)
      .subscribe((resp: HttpResponse<any>) => {
        checkToken(resp.headers);
        this.locationsMap = this.convertBodyToMap(resp);
        this.reloadCharts();
    });
  }

  convertBodyToMap(resp: HttpResponse<any>) : Map<any, any> {
    let map = new Map();
    for (let key of Object.keys(resp.body)) {
      map.set(new Date(key), resp.body[key]);
    }
    map = new Map([...map.entries()].sort(function compare(a, b) {
      var dateA = new Date(a[0]);
      var dateB = new Date(b[0]);
      return dateA.getTime() - dateB.getTime();
    }));
    return map;
  }

  setToWeek() {
    this.form.value.toDate = new Date();
    this.form.value.fromDate = new Date();
    this.form.value.fromDate.setDate(this.form.value.toDate.getDate() - this.form.value.toDate.getDay());
  }

  setToMonth() {
    this.form.value.toDate = new Date();
    this.form.value.fromDate = new Date();
    this.form.value.fromDate.setDate(1);
  }

  reloadCharts()
  {
    let i:number = 0;

    this.userChart.options.data[0].dataPoints = [];
    for (let [key, value] of this.userMap) {
      i += value;
      this.userChart.options.data[0].dataPoints.push({x: key, y: i});
    }
    this.userChart.render();

    i = 0;
    this.carrierChart.options.data[0].dataPoints = [];
    for (let [key, value] of this.carrierMap) {
      i += value;
      this.carrierChart.options.data[0].dataPoints.push({x: key, y: i});
    }
    this.carrierChart.render();

    this.locationsChart.options.data[0].dataPoints = [];
    for (let [key, value] of this.locationsMap) {
      i += value;
      this.locationsChart.options.data[0].dataPoints.push({x: key, y: i});
    }
    this.locationsChart.render();
  }

  ngOnInit() {
    this.userChart = new CanvasJS.Chart("userChartContainer", {
      animationEnabled: true,
      exportEnabled: true,
      title: {
        text: "Users change"
      },
      data: [{
        xValueFormatString: "YY-MM-DD",
        type: "spline",
        dataPoints: [
        ]
      }]
    });

    this.carrierChart = new CanvasJS.Chart("carrierChartContainer", {
      animationEnabled: true,
      exportEnabled: true,
      title: {
        text: "Carriers change"
      },
      data: [{
        xValueFormatString: "YY-MM-DD",
        type: "spline",
        dataPoints: [
        ]
      }]
    });

    this.locationsChart = new CanvasJS.Chart("locationChartContainer", {
      animationEnabled: true,
      exportEnabled: true,
      title: {
        text: "Locations change"
      },
      data: [{
        xValueFormatString: "YY-MM-DD",
        type: "spline",
        dataPoints: [
        ]
      }]
    });

    this.onSubmit();
  }

}
