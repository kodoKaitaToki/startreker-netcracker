import { Component, OnInit } from '@angular/core';
import { ApiDashboardService } from '../../../../../services/dashboards.service';
import { TripList, Trip } from '../../../../../services/interfaces/trip-dashboard.interface'
import { clone } from 'ramda';
import * as CanvasJS from '../../../../../../assets/js/canvasjs.min';

@Component({
  selector: 'app-trip-dashboard',
  templateUrl: './trip-dashboard.component.html',
  styleUrls: ['./trip-dashboard.component.scss']
})
export class TripDashboardComponent implements OnInit {

  title = 'Trips distribution';
  // tripData: Trip[] = [
  //   {
  //     departure_planet_name: 'Venus',
  //     arrival_planet_name: 'Mars',
  //     percentage: 25
  //   },
  //   {
  //     departure_planet_name: 'Earth',
  //     arrival_planet_name: 'Mercury',
  //     percentage: 75
  //   }
  // ]
  tripData: TripList;


  constructor(private tripService: ApiDashboardService) { }

  ngOnInit() {
    this.tripService.setTripDistribution();
    this.tripData = clone(this.tripService.getTrip);

    let chart = new CanvasJS.Chart("tripsChart", {
      animationEnabled: true,
      title:{
        text: "Trips distribution",
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
    this.tripData.trips.forEach(trip => {
        chart.options.data[0].dataPoints.push({y: trip.percentage, label: `From ${trip.departure_planet_name} to ${trip.arrival_planet_name}`})
    })
    
    chart.render();    
  }
}
