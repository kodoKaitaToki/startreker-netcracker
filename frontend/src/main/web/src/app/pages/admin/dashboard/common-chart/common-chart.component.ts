import { Component, OnInit } from '@angular/core';
import * as CanvasJS from '../../canvasjs.min';
import { Data } from '@angular/router';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-common-chart',
  templateUrl: './common-chart.component.html',
  styleUrls: ['./common-chart.component.scss'],
  providers: [DatePipe]
})
export class CommonChartComponent implements OnInit {

  butWeak :boolean;
  butMonth :boolean;
  checkBut = true;

  filterCriteria = [
    {name: 'id'},
    {name: 'name'},
    {name: 'status'},
  ];


  currentFilter = this.filterCriteria[0].name;
  currentFilterPlaceholder = `Search by ${this.currentFilter}`;

  weakDataPoints = [21,15,10];
  monthDataPoints = [71,55,50];
  intervalDataPoints = [100,100,100];

  dateFrom:Data;
  dateTo:Data;

  constructor(private datePipe: DatePipe) {
  }

  ngOnInit() {
    this.getWeakStat();
  }

	getStatistics(costAmount: Array<number>, subtitle: string){
	 	let chart = new CanvasJS.Chart("chartContainer", {
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

	getWeakStat(){
		this.butWeak = true;
		this.butMonth = false;
		this.getStatistics(this.weakDataPoints, 'per weak');
	}

	getMonthStat(){
		this.butWeak = false;
		this.butMonth = true;
		this.getStatistics(this.monthDataPoints, 'per month');
	}

	getInterStat(){
		this.butWeak = false;
		this.butMonth = false;
		let firstDate = this.datePipe.transform(this.dateFrom);
		let secondDate = this.datePipe.transform(this.dateTo);
		if((secondDate == null)||(secondDate==firstDate)){
			this.getStatistics(this.intervalDataPoints, firstDate);
		}else if(firstDate == null){
			this.getStatistics(this.intervalDataPoints, secondDate);
		}else if(firstDate > secondDate){
      this.getStatistics(this.intervalDataPoints, secondDate + ' - ' + firstDate);
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
