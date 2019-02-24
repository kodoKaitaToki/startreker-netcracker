import { Component, OnInit, Input } from '@angular/core';
import * as CanvasJS from '../../../../../../assets/js/canvasjs.min';
import { Data } from '@angular/router';
import { DatePipe } from '@angular/common';
import { DashCostService } from '../dashCostService';

@Component({
  selector: 'app-car-cost-dash',
  templateUrl: './car-cost-dash.component.html',
  styleUrls: ['./car-cost-dash.component.scss'],
  providers: [DatePipe]
})
export class CarCostDashComponent implements OnInit {

  @Input() carriers;

  carChoice = true;
  butWeak :boolean;
  butMonth :boolean;
  intervalBut = false;
  checkBut = true;

  filterCriteria = [
    {name: 'id'},
    {name: 'name'},
    {name: 'status'},
  ];

  filterContent = '';

  currentFilter = this.filterCriteria[0].name;
  currentFilterPlaceholder = `Search by ${this.currentFilter}`;

  weakDataPoints = [21,15,10];
  monthDataPoints = [71,55,50];
  intervalDataPoints = [100,100,100];

  dateFrom:Data;
  dateTo:Data;

  private dashCostService: DashCostService;

  constructor(private datePipe: DatePipe) { }

  ngOnInit() {
	this.getWeakStat();
  }

  setCarrier(){
	  this.carChoice = false;
  }

	getStatistics(costAmount: Array<number>, subtitle: string){
	 	let chart = new CanvasJS.Chart("chartContainerCar", {
			animationEnabled: true,
			exportEnabled: true,
			title: {
				text: subtitle
			},
			data: [{
				type: "column",
				dataPoints: [
					{ y: costAmount[0], label: "50$ tickets" },
					{ y: costAmount[1], label: "100$ tickets" },
					{ y: costAmount[2], label: "150$ tickets" }
				]
			}]
		});

		console.log(chart);

		chart.render();
	}

	chooseNewFilter(chosenFilterName) {

    	this.currentFilter = chosenFilterName.value;

    	this.currentFilterPlaceholder = `Search by ${this.currentFilter}`;
	  }

	getWeakStat(){
		this.butWeak = true;
		this.butMonth = false;

		/*let id;

		this.dashCostService
			.getCarrierPerWeak(id)
			.subscribe((data: {}) => {
				console.log(data);
			});*/


		this.getStatistics(this.carriers[0].weak, 'per weak');
	}

	getMonthStat(){
		this.butWeak = false;
		this.butMonth = true;

		/*let id;

		this.dashCostService
			.getCarrierPerMonth(id)
			.subscribe((data: {}) => {
				console.log(data);
			});*/



		this.getStatistics(this.carriers[0].month, 'per month');
	}

	getInterStat(){
		this.butWeak = false;
		this.butMonth = false;


		/*let id;
		let dateFrom = this.dateFrom.getFullYear() + '-' + this.dateFrom.getMonth() + this.dateFrom.getDate();
		let dateTo = this.dateTo.getFullYear() + '-' + this.dateTo.getMonth() + this.dateTo.getDate();

		this.dashCostService
			.getCarrierPerInterval(id, dateFrom, dateTo)
			.subscribe((data: {}) => {
				console.log(data);
			});*/
			
		let firstDate = this.datePipe.transform(this.dateFrom);
		let secondDate = this.datePipe.transform(this.dateTo);

		if(secondDate == null){
			this.getStatistics(this.intervalDataPoints, firstDate);
		}else if(firstDate == null){
			this.getStatistics(this.intervalDataPoints, secondDate);
		}else{
			this.getStatistics(this.intervalDataPoints, firstDate + ' - ' + secondDate);
		}
	}
	  
	  parseDate(dateString: string): Date {
		if (dateString) {
			this.checkBut = false;
			return new Date(dateString);
		} else {
			return null;
		}
	}

}
