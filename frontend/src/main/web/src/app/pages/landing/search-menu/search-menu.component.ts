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

  field = '';

  constructor(private landingService: LandingService) {
    this.searchForm = new FormGroup({
      startPlanet: new FormControl(''),
      finishPlanet: new FormControl(''),
      startSpaceport: new FormControl(''),
      finishSpaceport: new FormControl(''),
      startDate: new FormControl(''),
      finishDate: new FormControl('')
    });
   }

  ngOnInit() {
    this.getPlanets();
  }

  getPlanets(){
    this.landingService.getPlanets()
        .subscribe((resp: Response) => {
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

  onSubmit(){
    console.log(this.field);
    localStorage.setItem('formData', JSON.stringify(this.searchForm.value));
  }
}
