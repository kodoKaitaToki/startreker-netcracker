import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators} from '@angular/forms';
import { Router } from "@angular/router";
import { clone } from 'ramda';

import { LandingService } from '../shared/service/landing.service';
import { Planet } from '../shared/model/planet.model';
import { Spaceport } from '../../approver/trip/shared/model/spaceport.model';

declare function setPoint(point): any;

@Component({
  selector: 'app-search-menu',
  templateUrl: './search-menu.component.html',
  styleUrls: ['./search-menu.component.scss']
})
export class SearchMenuComponent implements OnInit {

  searchForm: FormGroup;

  planets: Planet[] = [];
  startPlanet: string;
  finishPlanet: string;
  spaceportsFrom: Spaceport[] = [];
  spaceportsTo: Spaceport[] = [];

  minimumDate = new Date();
  minFinishDate = new Date();

  field = '';

  constructor(private landingService: LandingService,
             private router: Router) {
    this.searchForm = new FormGroup({
      startPlanet: new FormControl({value: 'Choose a planet', disabled: true}),
      finishPlanet: new FormControl({value: 'Choose a planet', disabled: true}),
      startSpaceport: new FormControl({value: 'Choose a spaceport', disabled: true}),
      finishSpaceport: new FormControl({value: 'Choose a spaceport', disabled: true}),
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
          this.searchForm.get('startPlanet').enable();
          this.searchForm.get('finishPlanet').enable();
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
    localStorage.setItem('formData', JSON.stringify(this.searchForm.value));
    this.router.navigate(['/trip-search'])
  }
  
  getSpaceports(point: boolean){
    let planetName;
    if(point){
      this.searchForm.get('startSpaceport').disable();
      planetName = this.searchForm.get('startPlanet').value;
    }else{
      this.searchForm.get('finishSpaceport').disable();
      planetName = this.searchForm.get('finishPlanet').value;
    }
    for(let planet of this.planets){
      if(planet.planetName == planetName.toString().toUpperCase()){
        this.landingService.getSpaceports(planet.planetId)
          .subscribe((resp: Response) => {
            if(point){
              this.spaceportsFrom = clone(resp);
              this.searchForm.get('startSpaceport').enable();
            }else{
              this.spaceportsTo = clone(resp);
              this.searchForm.get('finishSpaceport').enable();
            }
        });
      }
    }
    
  }

}
