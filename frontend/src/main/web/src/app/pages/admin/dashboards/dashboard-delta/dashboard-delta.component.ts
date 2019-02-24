import { Component, OnInit } from '@angular/core';
import * as CanvasJS from '../../../../../assets/js/canvasjs.min';

import { FormGroup, FormControl } from '@angular/forms';
import {formatDate} from '@angular/common';

import { DashboardDeltaService } from '../dashboard-delta.service';

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
    fromDate: new FormControl(new Date()),
    toDate: new FormControl(new Date()),
  });

  constructor(private dashboardDeltaService: DashboardDeltaService) { }

  onSubmit() {
    let fromDateFormatted: string = formatDate(this.form.value.fromDate, 'yyyy-MM-dd', 'en');
    let toDateFormatted: string = formatDate(this.form.value.toDate, 'yyyy-MM-dd', 'en');

    this.dashboardDeltaService.getUsersIncreasingPerPeriod(fromDateFormatted, toDateFormatted).subscribe(data => {
      this.userMap = data;
      this.reloadCharts();
    });

    this.dashboardDeltaService.getCarriersIncreasingPerPeriod(fromDateFormatted, toDateFormatted).subscribe(data => {
      this.carrierMap = data;
      this.reloadCharts();
    });

    this.dashboardDeltaService.getLocationsIncreasingPerPeriod(fromDateFormatted, toDateFormatted).subscribe(data => {
      this.locationsMap = data;
      this.reloadCharts();
    });
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
    this.userChart.options.data[0].dataPoints = [];
    for (let [key, value] of this.userMap) {
      this.userChart.options.data[0].dataPoints.push({x: key, y: value});
    }
    this.userChart.render();

    this.carrierChart.options.data[0].dataPoints = [];
    for (let [key, value] of this.carrierMap) {
      this.carrierChart.options.data[0].dataPoints.push({x: key, y: value});
    }
    this.carrierChart.render();

    this.locationsChart.options.data[0].dataPoints = [];
    for (let [key, value] of this.locationsMap) {
      this.locationsChart.options.data[0].dataPoints.push({x: key, y: value});
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
