import {Component, OnInit} from '@angular/core';
import * as CanvasJS from '../../canvasjs.min';
import {FormGroup, FormControl} from '@angular/forms';
import {formatDate} from '@angular/common';
import {clone} from 'ramda';

import {HttpErrorResponse, HttpResponse} from '@angular/common/http';
import {ViewsModel} from "./viewsmodel.model";
import {ViewsService} from "./views.service";
import {TripEntry} from "./tripentry.class";
import {ServiceEntry} from "./serviceentry.class";
import {MessageService} from "primeng/api";
import {ShowMessageService} from "../../../admin/approver/shared/service/show-message.service";

@Component({
  selector: 'app-views',
  templateUrl: './views.component.html',
  styleUrls: ['./views.component.scss']
})
export class ViewsComponent implements OnInit {

  views: ViewsModel[];
  viewsChart: CanvasJS.Chart;

  carrierTrips: TripEntry[];
  carrierServices: ServiceEntry[];

  hideChart: boolean = true;

  fromDate: Date;
  toDate: Date;

  responseCallback = (resp: HttpResponse<any>) => {
    this.views = clone(resp.body);
    this.reloadCharts();
  };

  errorCallback = (error: HttpErrorResponse) => {
    this.showMsgSrvc.showMessage(this.messageService, 'error', `Error message - ${error.error.status}`,
      error.error.error
    );
  };

  form = new FormGroup({
    dataPeriod: new FormControl('perWeek'),
    forWhat: new FormControl('forTrips'),
    selectedTripId: new FormControl("null"),
    selectedServiceId: new FormControl("null"),
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

  constructor(private viewsService: ViewsService, private messageService: MessageService,
    private showMsgSrvc: ShowMessageService) {
  }

  onSubmit() {
    let fromDateFormatted: string;
    let toDateFormatted: string;
    try {
      fromDateFormatted = formatDate(this.fromDate, 'yyyy-MM-dd', 'en');
      toDateFormatted = formatDate(this.toDate, 'yyyy-MM-dd', 'en');
    } catch (e) {
      this.showMsgSrvc.showMessage(this.messageService, 'error', `Incorrect date`, `Please check your date`
      );
      return;
    }
    if (fromDateFormatted > toDateFormatted) {
      this.showMsgSrvc.showMessage(this.messageService,
        'error',
        `Incorrect date`,
        `Starting date is bigger than ending date`
      );
      return;
    }
    this.hideChart = false;

    if (this.form.value.forWhat == "forTrips") {
      return this.loadForTrips(fromDateFormatted, toDateFormatted);
    } else {
      return this.loadForServices(fromDateFormatted, toDateFormatted);
    }
  }

  loadForTrips(fromDateFormatted, toDateFormatted) {
    if (this.form.value.selectedTripId != "null") {
      return this.loadForTripsByTrip(this.form.value.selectedTripId,
        fromDateFormatted,
        toDateFormatted
      );
    }

    if (this.form.value.dataPeriod == "perMonth") {

      this.viewsService.getTripsViewsStatisticsByMonth(fromDateFormatted,
        toDateFormatted
      )
          .subscribe(this.responseCallback, this.errorCallback);
    } else {

      this.viewsService.getTripsViewsStatisticsByWeek(
        fromDateFormatted,
        toDateFormatted
      )
          .subscribe(this.responseCallback, this.errorCallback);
    }
  }

  loadForServices(fromDateFormatted, toDateFormatted) {
    if (this.form.value.selectedServiceId != "null") {
      return this.loadForServicesByService(this.form.value.selectedServiceId,
        fromDateFormatted,
        toDateFormatted
      );
    }

    if (this.form.value.dataPeriod == "perMonth") {

      this.viewsService.getServicesViewsStatisticsByMonth(fromDateFormatted,
        toDateFormatted
      )
          .subscribe(this.responseCallback, this.errorCallback);
    } else {

      this.viewsService.getServicesViewsStatisticsByWeek(fromDateFormatted,
        toDateFormatted
      )
          .subscribe(this.responseCallback, this.errorCallback);
    }
  }

  loadForTripsByTrip(tripId, fromDateFormatted, toDateFormatted) {
    if (this.form.value.dataPeriod == "perMonth") {

      this.viewsService.getTripsViewsStatisticsByMonthByTrip(tripId,
        fromDateFormatted,
        toDateFormatted
      )
          .subscribe(this.responseCallback, this.errorCallback);
    } else {

      this.viewsService.getTripsViewsStatisticsByWeekByTrip(tripId,
        fromDateFormatted,
        toDateFormatted
      )
          .subscribe(this.responseCallback, this.errorCallback);
    }
  }

  loadForServicesByService(serviceId, fromDateFormatted, toDateFormatted) {
    if (this.form.value.dataPeriod == "perMonth") {

      this.viewsService.getServicesViewsStatisticsByMonthByService(serviceId, fromDateFormatted,
        toDateFormatted
      )
          .subscribe(this.responseCallback, this.errorCallback);
    } else {

      this.viewsService.getServicesViewsStatisticsByWeekByService(serviceId, fromDateFormatted,
        toDateFormatted
      )
          .subscribe(this.responseCallback, this.errorCallback);
    }
  }

  setToWeek() {
    this.toDate = new Date();
    this.fromDate = new Date();
    this.fromDate.setDate(this.toDate.getDate() - 7);
  }

  setToMonth() {
    this.toDate = new Date();
    this.fromDate = new Date();
    this.fromDate.setDate(1);
  }

  ngOnInit() {

    this.viewsService.getCarrierTrips()
        .subscribe((trips) => {
          this.carrierTrips = trips;
        }, this.errorCallback);

    this.viewsService.getCarrierServices()
        .subscribe((services) => {
          this.carrierServices = services;
        }, this.errorCallback);

    this.setToMonth();

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
  }
}

