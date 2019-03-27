import { Component, OnInit } from '@angular/core';
import * as CanvasJS from '../../canvasjs.min';
import { FormGroup, FormControl } from '@angular/forms';
import {formatDate} from '@angular/common';
import { TRIP_SALES } from './mock_data';
import { SERVICE_SALES } from './mock_data';
import { SalesModel } from './sales.model';
import { clone } from 'ramda';

import { SalesService } from '../sales.service';
import { HttpResponse } from '@angular/common/http';
import { checkToken } from '../../../../modules/api';
import { MessageService } from 'primeng/api';

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
    this.soldChart.options.data[1].dataPoints = [];
    for (let model of this.tripSales) {
      this.soldChart.options.data[0].dataPoints.push({x: new Date((model.to.getTime() + model.from.getTime())/2), y: model.sold});
    }
    for (let model of this.serviceSales) {
      this.soldChart.options.data[1].dataPoints.push({x: new Date((model.to.getTime() + model.from.getTime())/2), y: model.sold});
    }
    this.soldChart.render();

    this.revenueChart.options.data[0].dataPoints = [];
    this.revenueChart.options.data[1].dataPoints = [];
    for (let model of this.tripSales) {
      this.revenueChart.options.data[0].dataPoints.push({x: new Date((model.to.getTime() + model.from.getTime())/2), y: model.revenue});
    }
    for (let model of this.serviceSales) {
      this.revenueChart.options.data[1].dataPoints.push({x: new Date((model.to.getTime() + model.from.getTime())/2), y: model.revenue});
    }
    this.revenueChart.render();
  }

  constructor(
    private salesService: SalesService,
    private messageService: MessageService) {
  }

  onSubmit() {
    let fromDateFormatted: string = formatDate(this.form.value.fromDate, 'yyyy-MM-dd', 'en');
    let toDateFormatted: string = formatDate(this.form.value.toDate, 'yyyy-MM-dd', 'en');

    if(this.form.value.dataPeriod != "perMonth")
    {
      this.salesService.getServicesSalesStatisticsByWeek(fromDateFormatted, toDateFormatted).subscribe(
        (resp: HttpResponse<any>) => {
          checkToken(resp.headers);
          if (resp.body.length == 0) {
            this.showMessage(this.createMessage('error', `Error message - 404`, "no sales found"));
          }
          else {
            this.serviceSales = this.convertFromDto(clone(resp.body));
            this.reloadCharts();
          }
      });
      this.salesService.getTripsSalesStatisticsByWeek(fromDateFormatted, toDateFormatted).subscribe(
        (resp: HttpResponse<any>) => {
          if (resp.body.length == 0) {
            this.showMessage(this.createMessage('error', `Error message - 404`, "no sales found"));
          }
          else {
            this.tripSales = this.convertFromDto(clone(resp.body));
            this.reloadCharts();
          }
      });
    }
    else
    {
      this.salesService.getServicesSalesStatisticsByMonth(fromDateFormatted, toDateFormatted).subscribe(
        (resp: HttpResponse<any>) => {
          checkToken(resp.headers);
          if (resp.body.length == 0) {
            this.showMessage(this.createMessage('error', `Error message - 404`, "no sales found"));
          }
          else {
            this.serviceSales = this.convertFromDto(clone(resp.body));
            this.reloadCharts();
          }
      });
      this.salesService.getTripsSalesStatisticsByMonth(fromDateFormatted, toDateFormatted).subscribe(
        (resp: HttpResponse<any>) => {
          checkToken(resp.headers);
          if (resp.body.length == 0) {
            this.showMessage(this.createMessage('error', `Error message - 404`, "no sales found"));
          }
          else {
            this.tripSales = this.convertFromDto(clone(resp.body));
            this.reloadCharts();
          }
      });
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

  convertFromDto(body: Array<any>) {
    let salesArray: SalesModel[] = body.map(item => {
      return new SalesModel(
          item.sold,
          item.revenue,
          new Date(item.from),
          new Date(item.to),
      );
    });
    console.log(body);
    console.log(salesArray);
    return salesArray;
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
    this.onSubmit();
  }

  showMessage(msgObj: any) {
    this.messageService.add(msgObj);
  }

  createMessage(severity: string, summary: string, detail: string): any {
    return {
      severity: severity,
      summary: summary,
      detail: detail
    };
  }

}
