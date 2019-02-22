import { Component, OnInit } from '@angular/core';
import * as CanvasJS from '../../canvasjs.min';
import { CARRIER_DATA } from './mock_data';
import { USER_DATA } from './mock_data';
import { LOCATION_DATA } from './mock_data';

import { DashboardDeltaService } from '../dashboard-delta.service';

@Component({
  selector: 'app-dashboard-delta',
  templateUrl: './dashboard-delta.component.html',
  styleUrls: ['./dashboard-delta.component.scss']
})
export class DashboardDeltaComponent implements OnInit {

  userMap: Map<string, number> = new Map();
  userChart: CanvasJS.Chart;

  constructor(private dashboardDeltaService: DashboardDeltaService) { }

  onSubmit() {
    this.dashboardDeltaService.getUsersIncreasingPerPeriod("2019-01-01", "2019-02-22").subscribe(data => {
      this.userMap = data;
      this.reloadCharts();
    });
  }

  reloadCharts()
  {
    this.userChart.options.data[0].dataPoints = [];
    for (let [key, value] of this.userMap) {
      this.userChart.options.data[0].dataPoints.push({y: value, label: key});
    }
    this.userChart.render();
  }

  ngOnInit() {
    this.userChart = new CanvasJS.Chart("userChartContainer", {
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

    this.onSubmit();
  }

}
