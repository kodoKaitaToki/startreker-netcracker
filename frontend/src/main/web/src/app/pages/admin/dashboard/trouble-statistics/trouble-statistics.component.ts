import { Component, OnInit } from '@angular/core';
import * as CanvasJS from '../../canvasjs.min';
import { TroubleStatisticsService } from '../trouble-statistics.service';
import { TroubleStatisticsModel } from './trouble-statistics.model';

@Component({
  selector: 'app-trouble-statistics',
  templateUrl: './trouble-statistics.component.html',
  styleUrls: ['./trouble-statistics.component.scss']
})
export class TroubleStatisticsComponent implements OnInit {
  troubleStatisticsModel: TroubleStatisticsModel;

  constructor(private troubleStatisticsService: TroubleStatisticsService) { }

  ngOnInit() {
    this.troubleStatisticsService.getStatistic().subscribe(data => {
          this.troubleStatisticsModel = data;
        });

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
                { y: this.troubleStatisticsModel.total_reopened, label: "Reopened" },
                { y: this.troubleStatisticsModel.total_opened, label: "Opened" },
                { y: this.troubleStatisticsModel.total_rated, label: "Rated" },
                { y: this.troubleStatisticsModel.total_answered, label: "Answered"},
                { y: this.troubleStatisticsModel.total_in_progress, label: "In progress"},
              ]
            }]
          });

    ticketChart.render();

  }

}
