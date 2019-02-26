import {Component, OnInit} from '@angular/core';
import * as CanvasJS from '../../canvasjs.min';
import {TroubleStatisticsService} from '../trouble-statistics.service';
import {TroubleStatisticsModel} from './trouble-statistics.model';
import {FormControl, FormGroup} from '@angular/forms';

@Component({
             selector: 'app-trouble-statistics',
             templateUrl: './trouble-statistics.component.html',
             styleUrls: ['./trouble-statistics.component.scss']
           })
export class TroubleStatisticsComponent implements OnInit {
  troubleStatisticsModel: TroubleStatisticsModel;

  ticketChart: CanvasJS.Chart;

  form = new FormGroup({
                         isTotal: new FormControl('total'),
                         id: new FormControl(''),
                       });

  constructor(private troubleStatisticsService: TroubleStatisticsService) {
  }

  onSubmit() {
    if (this.form.value.isTotal == "true" || this.form.value.id == "") {
      this.troubleStatisticsService.getStatistic()
          .subscribe(data => {
            this.troubleStatisticsModel = data;
            this.ticketChart.options.title.text = "Tickets:";
            this.reloadChart();
          });
    }
    else {
      this.troubleStatisticsService.getStatisticForApprover(this.form.value.id)
          .subscribe(data => {
            this.troubleStatisticsModel = data;
            this.ticketChart.options.title.text = "Tickets for approver id " + this.form.value.id + ":";
            this.reloadChart();
          });
    }
  }

  reloadChart() {
    this.ticketChart.options.data[0].dataPoints = [];
    this.ticketChart.options.data[0].dataPoints.push(
      {
        y: this.troubleStatisticsModel.total_reopened,
        label: "Reopened"
      }
    );
    this.ticketChart.options.data[0].dataPoints.push(
      {
        y: this.troubleStatisticsModel.total_opened,
        label: "Opened"
      }
    );
    this.ticketChart.options.data[0].dataPoints.push(
      {
        y: this.troubleStatisticsModel.total_rated,
        label: "Rated"
      }
    );
    this.ticketChart.options.data[0].dataPoints.push(
      {
        y: this.troubleStatisticsModel.total_answered,
        label: "Answered"
      }
    );
    this.ticketChart.options.data[0].dataPoints.push(
      {
        y: this.troubleStatisticsModel.total_in_progress,
        label: "In progress"
      }
    );
    setTimeout(() => {
      this.ticketChart.render();
    }, 100)
  }

  ngOnInit() {
    this.ticketChart = new CanvasJS.Chart("ticketChartContainer", {
      animationEnabled: true,
      exportEnabled: true,
      title: {
        text: "Categories:"
      },
      data: [{
        type: "doughnut",
        startAngle: 60,
        indexLabelFontSize: 17,
        indexLabel: "{label} - #percent%",
        toolTipContent: "<b>{label}:</b> {y} (#percent%)",
        dataPoints: []
      }]
    });
    this.onSubmit();
  }

}
