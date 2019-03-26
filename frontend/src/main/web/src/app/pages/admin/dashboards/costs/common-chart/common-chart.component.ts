import { Component, OnInit } from '@angular/core';
import * as CanvasJS from '../../../../../../assets/js/canvasjs.min';
import { Data } from '@angular/router';
import { DatePipe } from '@angular/common';
import { DashCostService } from '../dashCostService';
import { clone } from 'ramda'
import {checkToken} from "../../../../../modules/api/index";
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'app-common-chart',
  templateUrl: './common-chart.component.html',
  styleUrls: ['./common-chart.component.scss'],
  providers: [DatePipe]
})
export class CommonChartComponent implements OnInit{

	buttons = {
		butWeek: true,
		butMonth: false,
		checkBut: true
	}
	dateInvalid: boolean = false;

	title: string;

  	dateFrom:Data;
	dateTo:Data;

	startDate;
	finishDate;

	curStartDate: Date;
	curFinishDate: Date;
	minDate: Date;

  constructor(private datePipe: DatePipe, private dashCostService: DashCostService) {
  }

  ngOnInit() {
    this.getWeakStat();
	}

	getStatistics(costs){
	 	let chart = new CanvasJS.Chart("chartContainer", {
			animationEnabled: true,
			exportEnabled: true,
			title: {
				text: this.title
			},
			data: [{
				type: "column",
				dataPoints: costs
			}]
		});

		chart.render();
	}

	getWeakStat(){
		this.buttons.butWeek = true;
		this.buttons.butMonth = false;

		let curDate = new Date();
		let lastDate = new Date();
		curDate.setMonth(curDate.getMonth()+1);
		lastDate.setMonth(lastDate.getMonth()+1);
		lastDate.setDate(curDate.getDate()-7);
		let dateFrom = this.setDate(lastDate);
		let dateTo = this.setDate(curDate);

		this.title = 'per week';

		this.getCosts(dateFrom, dateTo);
	}

	getMonthStat(){
		this.buttons.butWeek = false;
		this.buttons.butMonth = true;

		let curDate = new Date();
		let lastDate = new Date();
		curDate.setMonth(curDate.getMonth()+1);
		let dateFrom = this.setDate(lastDate);
		let dateTo = this.setDate(curDate);

		this.title = 'per month';

		this.getCosts(dateFrom, dateTo);
	}

	getInterStat(){
		this.buttons.butWeek = false;
		this.buttons.butMonth = false;
		let firstDateTitle = this.datePipe.transform(this.curStartDate);
		let secondDateTitle = this.datePipe.transform(this.curFinishDate);
		this.title = firstDateTitle + ' - ' + secondDateTitle;
		this.getCosts(this.curStartDate.toISOString(), this.curFinishDate.toISOString());
	}


	setDate(date: Date){
		return date.getFullYear()
					+ '-' + (date.getMonth() < 10 ? '0' : '')
					+ date.getMonth()
					+ '-' + date.getDate();
	}

	private getCosts(from, to){
    	this.dashCostService.getCosts(from, to)
                        .subscribe(
                          (resp: HttpResponse<any>) => {
                            checkToken(resp.headers);
							let response = clone(resp.body);
							let tickets = this.dashCostService.parseResponse(response);
							this.getStatistics(tickets);
                          }
                        );
  }

  onSelect(event){
    if(this.curFinishDate < event){
			this.curFinishDate = undefined;
    }
		this.minDate = event;
		this.checkBtn();
  }

  checkBtn(){
	if((this.curFinishDate !== undefined) && (this.curStartDate !== undefined)){
		this.buttons.checkBut = false;
	}
  }
}
