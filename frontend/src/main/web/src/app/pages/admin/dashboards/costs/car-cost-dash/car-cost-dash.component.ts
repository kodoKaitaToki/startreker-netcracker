import { Component, OnInit, Input } from '@angular/core';
import * as CanvasJS from '../../../../../../assets/js/canvasjs.min';
import { Data } from '@angular/router';
import { DatePipe } from '@angular/common';
import {CarrierCrudService} from '../../../carrier/carrier-crud.service';
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
  emptyResp: boolean = true;

  title: string = '';

  constructor(private datePipe: DatePipe,
				private carCrudService: CarrierCrudService,
				private dashCostService: DashCostService) { }

  ngOnInit() {
	this.getCarriers();
	this.getWeakStat();
  }

	getStatistics(costs){
		if(costs.length > 0){
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

		let firstDateTitle = this.datePipe.transform(this.dateFrom);
		let secondDateTitle = this.datePipe.transform(this.dateTo);

		this.setDate(this.startDate, this.finishDate);

		this.title = firstDateTitle + ' - ' + secondDateTitle;
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

	parseDate(dateString: string, flag): Date {
		if (dateString) {

			if(flag){
				this.startDate = new Date(dateString);
			}else{
				this.finishDate = new Date(dateString);
			}

			if(this.finishDate < this.startDate){
				this.dateInvalid = true;
				this.buttons.checkBut = true;
			}else{
				this.dateInvalid = false;
				if((this.startDate !== undefined) && (this.finishDate !== undefined)){
					this.buttons.checkBut = false;
				}
			}

			return new Date(dateString);
		} else {
			return null;
		}
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
		if(this.curCarrier !== undefined){

			this.dashCostService.getCarCosts(this.curId, this.curDateFrom, this.curDateTo)
													.subscribe(
														(resp: HttpResponse<any>) => {
                              								checkToken(resp.headers);
															let response = clone(resp.body);
															let tickets = this.dashCostService.parseResponse(response);
															this.getStatistics(tickets);
														}
													);
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
}
