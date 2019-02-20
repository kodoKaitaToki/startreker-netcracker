import { Component, OnInit } from '@angular/core';
import * as CanvasJS from '../canvasjs.min';

@Component({
  selector: 'app-trouble-statistics',
  templateUrl: './trouble-statistics.component.html',
  styleUrls: ['./trouble-statistics.component.scss']
})
export class TroubleStatisticsComponent implements OnInit {

  constructor() { }

  ngOnInit() {
    let ticketChart = new CanvasJS.Chart("ticketChartContainer", {
            animationEnabled: true,
            exportEnabled: true,
            title: {
              text: "Trouble ticket categories:"
            },
            data: [{
              type: "doughnut",
              startAngle: 60,
              indexLabelFontSize: 17,
              indexLabel: "{label} - #percent%",
              toolTipContent: "<b>{label}:</b> {y} (#percent%)",
              dataPoints: [
                { y: 1, label: "Reopened" },
                { y: 1, label: "Opened" },
                { y: 1, label: "Rated" },
                { y: 1, label: "Answered"},
                { y: 1, label: "In progress"},
              ]
            }]
          });

    ticketChart.render();

  }

}
