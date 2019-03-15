import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {ReactiveFormsModule} from '@angular/forms';

@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.scss'],
})
export class HistoryComponent implements OnInit {

  public searchParams: FormGroup;
  public data: any;
  public page: number = 1;

  constructor() {
  }

  ngOnInit() {

    this.searchParams = new FormGroup({
      beforeDate: new FormControl(""),
      afterDate: new FormControl(""),
    });

    this.data = [
      {
        'ticket': {
          'id': '1',
          'departure_spaceport_name': 'ZHULANIU',
          'arrival_spaceport_name': 'LUZK',
          'departure_planet_name': 'MOON',
          'arrival_planet_name': 'VENUS',
          'departure_date': '2015-03-05  15:32',
          'arrival_date': '2015-03-06  04:22',
          'purchase_date': '2015-03-01  11:52',
          'class': 'Business',
          'price': '100000 $',
          'carrier_name': 'UKRSPACE',
          'seat': '15',
          'services': [
            {
              'service_name': 'Tea'
            },
            {
              'service_name': 'Additional space'
            }
          ]
        }
      },

      {
        'ticket': {
          'id': '2',
          'departure_spaceport_name': 'ALLAS',
          'arrival_spaceport_name': 'ILLA',
          'departure_planet_name': 'PLUTO',
          'arrival_planet_name': 'NIBIRU',
          'departure_date': '2016-01-03  21:32',
          'arrival_date': '2018-03-10  022:22',
          'purchase_date': '2011-02-01  10:11',
          'class': 'Economy',
          'price': '99999999 $',
          'carrier_name': 'NIBIRUEXPRESS',
          'seat': '-1',
          'services': [
            {
              'service_name': 'Prayers'
            },
            {
              'service_name': 'Tea'
            },
          ]
        }
      },

      {
        'ticket': {
          'id': '3',
          'departure_spaceport_name': 'JIA',
          'arrival_spaceport_name': 'ALLATR',
          'departure_planet_name': 'MARS',
          'arrival_planet_name': 'PLUTO',
          'departure_date': '2015-03-03  01:32',
          'arrival_date': '2015-03-10  09:22',
          'purchase_date': '2015-02-01  10:11',
          'class': 'First',
          'price': '10000000 $',
          'carrier_name': 'MARSFLY',
          'seat': '04',
          'services': [
            {
              'service_name': 'Food'
            },
            {
              'service_name': 'Air'
            }
          ]
        },
      },
      {
        'bundle': {
          'id': '1',
          'purchase_date': '2015-03-12  23:11',
          'endprice': '23211',
          'tickets': [
            {
              'id': '5',
              'departure_spaceport_name': 'JIA',
              'arrival_spaceport_name': 'ALLATR',
              'departure_planet_name': 'MARS',
              'arrival_planet_name': 'URANUS',
              'departure_date': '2015-03-03  01:32',
              'arrival_date': '2015-03-10  09:22',
              'purchase_date': '2015-02-01  10:11',
              'class': 'First',
              'price': '10000000 $',
              'carrier_name': 'MARSFLY',
              'seat': '04',
              'services': [
                {
                  'service_name': 'Food'
                },
                {
                  'service_name': 'Air'
                }
              ]
            },

            {
              'id': '6',
              'departure_spaceport_name': 'JIBRAL',
              'arrival_spaceport_name': 'EENETA',
              'departure_planet_name': 'URANUS',
              'arrival_planet_name': 'NEWTON',
              'departure_date': '2015-03-03  01:32',
              'arrival_date': '2015-03-10  09:22',
              'purchase_date': '2015-02-01  10:11',
              'class': 'First',
              'price': '1122 $',
              'carrier_name': 'DONBOI',
              'seat': '14',
              'services': [
                {
                  'service_name': 'Food'
                },
                {
                  'service_name': 'Kiki'
                }
              ]
            }

          ]
        },
      }
    ];

    //this.ticketList = [];
  }

  search(data) {
    console.log(data);
  }

  onPageChange(event) {
    this.page = event;
  }

}
