import { Component, OnInit} from '@angular/core';
import * as CanvasJS from '../../../../../../assets/js/canvasjs.min';
import { Data } from '@angular/router';
import { DatePipe } from '@angular/common';
import {CarrierCrudService} from '../../../carrier/shared/service/carrier-crud.service';
import { clone } from 'ramda'
import { DashCostService } from '../dashCostService';
import {checkToken} from "../../../../../modules/api/index";
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'app-car-cost-dash',
  templateUrl: './car-cost-dash.component.html',
  styleUrls: ['./car-cost-dash.component.scss'],
  providers: [DatePipe]
})
export class CarCostDashComponent implements OnInit {

  buttons = {
	butWeek: true,
	butMonth: false,
	checkBut: true
	}
	dateInvalid: boolean = false;

  dateFrom:Data;
  dateTo:Data;

  startDate;
	finishDate;

  carriers;
  curCarrier;
  curId: number;
  curName: string = '';
  curDateFrom: string;
  curDateTo: string;
  emptyResp: boolean = false;

  curStartDate: Date;
	curFinishDate: Date;
	minDate: Date;

  title: string = '';
  chart: boolean = false;

  constructor(private datePipe: DatePipe,
				private carCrudService: CarrierCrudService,
				private dashCostService: DashCostService) { }

  ngOnInit() {
	this.getCarriers();
	this.getWeakStat();
  }

	getStatistics(costs){
		if(costs.length > 0){
			this.chart = true;
			this.emptyResp = false;
			let chart = new CanvasJS.Chart("chartContainerCar", {
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
		}else{
			this.emptyResp = true;
		}

	}

	getWeakStat(){
		this.buttons.butWeek = true;
		this.buttons.butMonth = false;

		let curDate = new Date();
		let lastDate = new Date();
		curDate.setMonth(curDate.getMonth()+1);
		lastDate.setMonth(lastDate.getMonth()+1);
		lastDate.setDate(curDate.getDate()-7);
		this.setDate(lastDate, curDate);

		this.title = 'per week';
	}

	getMonthStat(){
		this.buttons.butWeek = false;
		this.buttons.butMonth = true;

		let curDate = new Date();
		let lastDate = new Date();
		curDate.setMonth(curDate.getMonth()+1);
		this.setDate(lastDate, curDate);

		this.title = 'per month';
	}

	getInterStat(){
		this.buttons.butWeek = false;
		this.buttons.butMonth = false;

		let firstDateTitle = this.datePipe.transform(this.curStartDate);
		let secondDateTitle = this.datePipe.transform(this.curFinishDate);
		this.title = firstDateTitle + ' - ' + secondDateTitle;

		this.curDateFrom = this.curStartDate.toISOString();
		this.curDateTo = this.curFinishDate.toISOString();
	}

	setDate(dateFrom: Date, dateTo: Date){
		this.curDateFrom = dateFrom.getFullYear()
							+ '-' + (dateFrom.getMonth() < 10 ? '0' : '')
							+ dateFrom.getMonth()
							+ '-' + dateFrom.getDate();
		this.curDateTo = dateTo.getFullYear()
							+ '-' + (dateFrom.getMonth() < 10 ? '0' : '')
							+ dateTo.getMonth()
							+ '-' + dateTo.getDate();
	}

	getCarriers(){
		this.carCrudService.getAllCarriers()
                      .subscribe(
                        (resp: HttpResponse<any>) => {
                          checkToken(resp.headers);
						    					this.carriers = clone(resp.body);
                        }
                      );
	}

	getCarCosts(){
		if((this.curCarrier !== undefined) && (this.curCarrier !== 'Choose a carrier')){
			this.chart = false;
			this.dashCostService.getCarCosts(this.curId, this.curDateFrom, this.curDateTo)
								.subscribe((resp: HttpResponse<any>) => {
                              		checkToken(resp.headers);
									let response = clone(resp.body);
									let tickets = this.dashCostService.parseResponse(response);
									this.getStatistics(tickets);
								});
		}
	}

	getCarrierByName(event){
		this.curName = event.value;
		this.carCrudService.getCarrierByUsername(event.value)
                      .subscribe(
                        (resp: HttpResponse<any>) => {
                          checkToken(resp.headers);
						              this.curCarrier = clone(resp.body);
						              this.getId();
                        }
                      );
	}

	getId(){
		this.curId = this.curCarrier.id;
		this.getCarCosts();
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
