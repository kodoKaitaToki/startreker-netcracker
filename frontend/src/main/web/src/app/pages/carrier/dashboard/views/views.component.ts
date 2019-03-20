import {Component, OnInit} from '@angular/core';
import * as CanvasJS from '../../canvasjs.min';
import {FormGroup, FormControl} from '@angular/forms';
import {formatDate} from '@angular/common';
import {clone} from 'ramda';

import {HttpResponse} from '@angular/common/http';
import {ViewsModel} from "./viewsmodel.model";
import {ViewsService} from "./views.service";
import {TripEntry} from "./tripentry.class";

@Component({
  selector: 'app-views',
  templateUrl: './views.component.html',
  styleUrls: ['./views.component.scss']
})
export class ViewsComponent implements OnInit {

  views: ViewsModel[];
  viewsChart: CanvasJS.Chart;

  carrierTrips: TripEntry[];

  responseCallback = (resp: HttpResponse<any>) => {
    this.views = clone(resp);
    this.reloadCharts();
  };

  form = new FormGroup({
    dataPeriod: new FormControl('perWeek'),
    forWhat: new FormControl('forTrips'),
    fromDate: new FormControl(new Date()),
    toDate: new FormControl(new Date()),
    selectedTripId: new FormControl(null),
    selectedServiceId: new FormControl(null),
  });

  reloadCharts() {
    this.viewsChart.options.data[0].dataPoints = [];
    this.viewsChart.options.data[1].dataPoints = [];
    for (let model of this.views) {
      this.viewsChart.options.data[0].dataPoints.push({
        x: new Date((model.to.getTime() + model.from.getTime()) / 2),
        y: model.views
      });
    }
    this.viewsChart.render();
  }

  constructor(private viewsService: ViewsService) {
    this.viewsService.getCarrierTrips()
        .subscribe((trips) => {
          this.carrierTrips = trips;
        });
  }

  onSubmit() {
    let fromDateFormatted: string = formatDate(this.form.value.fromDate, 'yyyy-MM-dd', 'en');
    let toDateFormatted: string = formatDate(this.form.value.toDate, 'yyyy-MM-dd', 'en');

    if (this.form.value.forWhat == "forTrips") {
      return this.loadForTrips(fromDateFormatted, toDateFormatted);
    } else {
      return this.loadForServices(fromDateFormatted, toDateFormatted);
    }
  }

  loadForTrips(fromDateFormatted, toDateFormatted) {
    if (this.form.value.selectedTripId != null) {
      return this.loadForTripsByTrip(this.form.value.selectedTripId,
        fromDateFormatted,
        toDateFormatted
      );
    }

    if (this.form.value.dataPeriod == "perMonth") {

      this.viewsService.getTripsViewsStatisticsByMonth(fromDateFormatted,
        toDateFormatted
      )
          .subscribe(this.responseCallback);
    } else {

      this.viewsService.getTripsViewsStatisticsByWeek(
        fromDateFormatted,
        toDateFormatted
      )
          .subscribe(this.responseCallback);
    }
  }

  loadForServices(fromDateFormatted, toDateFormatted) {
    if (this.form.value.selectedServiceId != null) {
      return this.loadForServicesByService(this.form.value.selectedServiceId,
        fromDateFormatted,
        toDateFormatted
      );
    }

    if (this.form.value.dataPeriod == "perMonth") {

      this.viewsService.getServicesViewsStatisticsByMonth(fromDateFormatted,
        toDateFormatted
      )
          .subscribe(this.responseCallback);
    } else {

      this.viewsService.getServicesViewsStatisticsByWeek(fromDateFormatted,
        toDateFormatted
      )
          .subscribe(this.responseCallback);
    }
  }

  loadForTripsByTrip(tripId, fromDateFormatted, toDateFormatted) {
    if (this.form.value.dataPeriod == "perMonth") {

      this.viewsService.getTripsViewsStatisticsByMonthByTrip(tripId,
        fromDateFormatted,
        toDateFormatted
      )
          .subscribe(this.responseCallback);
    } else {

      this.viewsService.getTripsViewsStatisticsByWeekByTrip(tripId,
        fromDateFormatted,
        toDateFormatted
      )
          .subscribe(this.responseCallback);
    }
  }

  loadForServicesByService(serviceId, fromDateFormatted, toDateFormatted) {
    if (this.form.value.dataPeriod == "perMonth") {

      this.viewsService.getServicesViewsStatisticsByMonthByService(serviceId, fromDateFormatted,
        toDateFormatted
      )
          .subscribe(this.responseCallback);
    } else {

      this.viewsService.getServicesViewsStatisticsByWeekByService(serviceId, fromDateFormatted,
        toDateFormatted
      )
          .subscribe(this.responseCallback);
    }
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

  ngOnInit() {
    this.viewsChart = new CanvasJS.Chart("viewsChartContainer", {
      animationEnabled: true,
      exportEnabled: true,
      title: {
        text: "Views"
      },
      data: [{
        xValueFormatString: "YY-MM-DD",
        type: "column",
        showInLegend: true,
        name: "Views",
        color: "#9BBB58",
        dataPoints: []
      },
        {
          xValueFormatString: "YY-MM-DD",
          type: "column",
          showInLegend: true,
          name: "Time",
          color: "#C0504E",
          dataPoints: []
        }]
    });

    this.onSubmit();
  }

}
