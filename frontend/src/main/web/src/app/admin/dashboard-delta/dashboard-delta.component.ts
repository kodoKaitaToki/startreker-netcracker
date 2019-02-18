import { Component, OnInit } from '@angular/core';
import * as CanvasJS from '../canvasjs.min';
import { DataModel } from './data_model';
import { CARRIER_DATA } from './mock_data';
import { USER_DATA } from './mock_data';
import { LOCATION_DATA } from './mock_data';

@Component({
  selector: 'app-dashboard-delta',
  templateUrl: './dashboard-delta.component.html',
  styleUrls: ['./dashboard-delta.component.scss']
})
export class DashboardDeltaComponent implements OnInit {

  constructor() { }

  ngOnInit() {
    let carrierChart = new CanvasJS.Chart("carrierChartContainer", {
        animationEnabled: true,
        exportEnabled: true,
        title: {
          text: "Carier change"
        },
        data: [{
          type: "line",
          dataPoints: [
          ]
        }]
      });

    let userChart = new CanvasJS.Chart("userChartContainer", {
      animationEnabled: true,
      exportEnabled: true,
      title: {
        text: "Users change"
      },
      data: [{
        type: "line",
        dataPoints: [
        ]
      }]
    });

    let locationChart = new CanvasJS.Chart("locationChartContainer", {
      animationEnabled: true,
      exportEnabled: true,
      title: {
        text: "Locations change"
      },
      data: [{
        type: "line",
        dataPoints: [
        ]
      }]
    });

    for (let d of CARRIER_DATA) {
      carrierChart.options.data[0].dataPoints.push({y: d[0], label: d[1]});
    }

    for (let d of USER_DATA) {
      userChart.options.data[0].dataPoints.push({y: d[0], label: d[1]});
    }

    for (let d of LOCATION_DATA) {
      locationChart.options.data[0].dataPoints.push({y: d[0], label: d[1]});
    }

    carrierChart.render();

    userChart.render();

    locationChart.render();
  }

}
