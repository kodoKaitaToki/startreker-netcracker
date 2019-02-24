import { Component, OnInit } from '@angular/core';
import * as CanvasJS from '../../canvasjs.min';
import { FormGroup, FormControl } from '@angular/forms';
import { TRIP_SALES } from './mock_data';
import { SERVICE_SALES } from './mock_data';
import { SalesModel } from './sales.model';

@Component({
  selector: 'app-sales',
  templateUrl: './sales.component.html',
  styleUrls: ['./sales.component.scss']
})
export class SalesComponent implements OnInit {

  tripSales: SalesModel[];
  serviceSales: SalesModel[];
  soldChart: CanvasJS.Chart;
  revenueChart: CanvasJS.Chart;

  form = new FormGroup({
    dataPeriod: new FormControl('perWeek'),
    fromDate: new FormControl(new Date()),
    toDate: new FormControl(new Date()),
  });

  reloadCharts()
  {
    this.soldChart.options.data[0].dataPoints = [];
    for (let model of this.tripSales) {
      this.soldChart.options.data[0].dataPoints.push({x: new Date(2*model.to.getTime() - model.from.getTime()), y: model.sold});
    }
    for (let model of this.serviceSales) {
      this.soldChart.options.data[1].dataPoints.push({x: new Date(2*model.to.getTime() - model.from.getTime()), y: model.sold});
    }
    this.soldChart.render();

    this.revenueChart.options.data[0].dataPoints = [];
    for (let model of this.tripSales) {
      this.revenueChart.options.data[0].dataPoints.push({x: new Date(2*model.to.getTime() - model.from.getTime()), y: model.revenue});
    }
    for (let model of this.serviceSales) {
      this.revenueChart.options.data[1].dataPoints.push({x: new Date(2*model.to.getTime() - model.from.getTime()), y: model.revenue});
    }
    this.revenueChart.render();
  }

  constructor() {
    this.tripSales = TRIP_SALES;
    this.serviceSales = SERVICE_SALES;
  }

  ngOnInit() {
    this.soldChart = new CanvasJS.Chart("amountChartContainer", {
      animationEnabled: true,
      exportEnabled: true,
      title: {
        text: "Sold"
      },
      data: [{
        xValueFormatString: "YY-MM-DD",
        type: "column",
        showInLegend: true,
        name: "Trips",
        color: "#9BBB58",
        dataPoints: [
        ]
      },
      {
        xValueFormatString: "YY-MM-DD",
        type: "column",
        showInLegend: true,
        name: "Services",
        color: "#C0504E",
        dataPoints: [
        ]
      }]
    });

    this.revenueChart = new CanvasJS.Chart("revenueChartContainer", {
      animationEnabled: true,
      exportEnabled: true,
      title: {
        text: "Revenue"
      },
      data: [{
        xValueFormatString: "YY-MM-DD",
        type: "column",
        showInLegend: true,
        name: "Trips",
        color: "#9BBB58",
        dataPoints: [
        ]
      },
      {
        xValueFormatString: "YY-MM-DD",
        type: "column",
        showInLegend: true,
        name: "Services",
        color: "#C0504E",
        dataPoints: [
        ]
      }]
    });

    this.reloadCharts();
  }

}
