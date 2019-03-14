import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { clone } from 'ramda';

import { LandingService } from '../shared/service/landing.service';
import { Planet } from '../shared/model/planet.model';

declare function setPoint(point): any;

@Component({
  selector: 'app-search-menu',
  templateUrl: './search-menu.component.html',
  styleUrls: ['./search-menu.component.scss']
})
export class SearchMenuComponent implements OnInit {

  searchForm: FormGroup;

  planets: Planet[] = [];

  constructor(private landingService: LandingService) {
    this.searchForm = new FormGroup({
    });
   }

  ngOnInit() {
    this.getPlanets();
  }

  getPlanets(){
    this.landingService.getPlanets()
        .subscribe((resp: Response) => {
          //Don't forget to add checkToken
          this.planets = clone(resp);
        })
  }

  setPoint(point){
    setPoint(point);
  }

  disableDatepicker(){
    let element = <HTMLInputElement> document.getElementById("returnDate");
    element.disabled = true;
  }

  enableDatepicker(){
    let element = <HTMLInputElement> document.getElementById("returnDate");
    element.disabled = false;
  }
}
