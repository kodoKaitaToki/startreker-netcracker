import {Component, OnInit} from '@angular/core';
import * as CanvasJS from '../../canvasjs.min';
import {TroubleStatisticsService} from '../trouble-statistics.service';
import {TroubleStatisticsModel} from './trouble-statistics.model';
import {FormControl, FormGroup} from '@angular/forms';
import { clone } from 'ramda';

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
          .subscribe((resp: Response) => {
            this.troubleStatisticsModel = clone(resp);
            this.ticketChart.options.title.text = "Tickets:";
            this.reloadChart();
          });
    }
    else {
      this.troubleStatisticsService.getStatisticForApprover(this.form.value.id)
          .subscribe((resp: Response) => {
            this.troubleStatisticsModel = clone(resp);
            this.ticketChart.options.title.text = "Tickets for approver id " + this.form.value.id + ":";
            this.reloadChart();
          });
    }
  }

  reloadChart() {
    this.ticketChart.options.data[0].dataPoints = [];
    this.addSectionToChart(this.troubleStatisticsModel.total_reopened, "Reopened");
    this.addSectionToChart(this.troubleStatisticsModel.total_opened, "Opened");
    this.addSectionToChart(this.troubleStatisticsModel.total_rated, "Rated");
    this.addSectionToChart(this.troubleStatisticsModel.total_answered, "Answered");
    this.addSectionToChart(this.troubleStatisticsModel.total_in_progress, "In progress");
    setTimeout(() => {
      this.ticketChart.render();
    }, 100)
  }

  addSectionToChart(value: number, sectionLabel: string) {
    this.ticketChart.options.data[0].dataPoints.push(
      {
        y: value,
        label: sectionLabel
      }
    );
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
