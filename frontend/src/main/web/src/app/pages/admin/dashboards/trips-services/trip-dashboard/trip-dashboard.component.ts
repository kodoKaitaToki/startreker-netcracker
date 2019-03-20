import { Component, OnInit } from '@angular/core';
import { ApiDashboardService } from '../../../../../services/dashboards.service';
import { TripList, Trip } from '../../../../../services/interfaces/trip-dashboard.interface'
import { clone } from 'ramda';
import * as CanvasJS from '../../../../../../assets/js/canvasjs.min';
import {checkToken} from "../../../../../modules/api/index";
import { HttpResponse } from '@angular/common/http';

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
  tripData: TripList = {
    trips: []
  };

  // get test() {
  //   return this.tripService.tripData;
  // }


  constructor(private tripService: ApiDashboardService) {}

  buildChart(tripData: TripList) {
    console.log('buildTrips')
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
    tripData.trips.forEach(trip => {
      chart.options.data[0].dataPoints.push({y: trip.percentage, label: `From ${trip.departure_planet_name} to ${trip.arrival_planet_name}`})
    })

    chart.render();
  }

  ngOnInit() {
      this.tripService.setTripDistribution()
      .subscribe(
        (resp: HttpResponse<any>) => {
          checkToken(resp.headers);
            console.log(resp.body)
            this.tripData.trips = clone(resp);
            // console.log(this.tripData.trips);
            this.buildChart(this.tripData);
        },
        error => console.log(error)
    );
    // this.tripData = clone(this.tripService.getTrip);
  }
}
