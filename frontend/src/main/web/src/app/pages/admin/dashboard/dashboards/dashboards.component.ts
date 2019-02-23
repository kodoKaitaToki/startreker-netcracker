import { Component, OnInit } from '@angular/core';

import { DashCostService } from '../dashCostService';
import { DashCost } from '../dashCost.entity';

@Component({
  selector: 'app-dashboards',
  templateUrl: './dashboards.component.html',
  styleUrls: ['./dashboards.component.scss']
})
export class DashboardsComponent implements OnInit {

  ticketPrices: DashCost = {
    50: 50,
    100: 100,
    150: 150
  };

  defaultCarriers = [];
  tickets: number[] = [];
  carTickets: number[] = [];
  carName: string = '';

  constructor(private dashCostService: DashCostService) { }

  ngOnInit() {
    this.defaultCarriers = this.getDefaultCarriers();
  }

  getDefaultCarriers(){

    return [
      {
        id: '1',
        weak: [50,30,10],
        month: [40,20,8]
      }
    ];
  }

  processCosts(event){
    this.getCosts(event.from, event.to);
  }

  processCarCosts(event){
    this.getCarCosts(event.id, event.from, event.to);
  }

  private getCosts(from, to){
    this.dashCostService.getCosts(from, to)
                        .subscribe(
                          (resp: Response) => {
                          /*if (resp.headers.get('New-Access-Token')) {
                            localStorage.removeItem('at');
                            localStorage.setItem('at', resp.headers.get('New-Access-Token'));
                          }*/
                            this.parseResponse(resp);
                          },
                          error => console.log(error)
                        );
  }

  private getCarCosts(id: number, from, to){
    this.dashCostService.getCarCosts(id, from, to)
                        .subscribe(
                          (resp: Response) => {
                          /*if (resp.headers.get('New-Access-Token')) {
                            localStorage.removeItem('at');
                            localStorage.setItem('at', resp.headers.get('New-Access-Token'));
                          }*/
                            this.parseResponse(resp);
                          },
                          error => console.log(error)
                        );
  }

  parseResponse(response){

    for (var price in this.ticketPrices){
      this.tickets.push(response[price]*this.ticketPrices[price]);
    }
    
  }

}
