import { Component, OnInit } from '@angular/core';
import { ApiDashboardService } from '../../../../../services/dashboards.service';
import { ServiceList, Service } from '../../../../../services/interfaces/service-dashboard.interface'
import { clone } from 'ramda';
import * as CanvasJS from '../../../../../../assets/js/canvasjs.min';

@Component({
  selector: 'app-service-dashboard',
  templateUrl: './service-dashboard.component.html',
  styleUrls: ['./service-dashboard.component.scss']
})
export class ServiceDashboardComponent implements OnInit {
  title = 'Services distribution';
  // serviceData: Service[] = [
  //   {
  //     service_name: 'serv1',
  //     percentage: 25
  //   },
  //   {
  //     service_name: 'serv2',
  //     percentage: 75
  //   }
  // ]
  serviceData: ServiceList;


  constructor(private serviceService: ApiDashboardService) { }

  ngOnInit() {
    this.serviceService.setTripDistribution();
    this.serviceData = clone(this.serviceService.getTrip);

    let chart = new CanvasJS.Chart("servicesChart", {
      animationEnabled: true,
      title:{
        text: "Services distribution",
        horizontalAlign: "center"
      },
      data: [{
        type: "doughnut",
        startAngle: 60,
        //innerRadius: 60,
        indexLabelFontSize: 17,
        indexLabel: "{label} - #percent%",
        toolTipContent: "<b>{label}:</b> {y} (#percent%)",
        dataPoints: []
      }]
    });
    this.serviceData.services.forEach(service => {
        chart.options.data[0].dataPoints.push({y: service.percentage, label: service.service_name})
    })
    
    chart.render();    
  }
}
