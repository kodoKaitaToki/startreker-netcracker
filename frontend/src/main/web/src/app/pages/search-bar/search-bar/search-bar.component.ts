import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { LandingService } from '../../landing/shared/service/landing.service';
import { Planet } from '../../landing/shared/model/planet.model';
import { FormGroup, FormControl } from '@angular/forms';
import { Spaceport } from 'src/app/pages/landing/shared/model/spaceport.model';
import { Router } from '@angular/router';
import { DataService } from '../../../shared/data.service';
import { clone } from 'ramda';
import { Location } from '@angular/common';

declare function setPoint(point): any;

@Component({
  selector: 'app-search-bar',
  templateUrl: './search-bar.component.html',
  styleUrls: ['./search-bar.component.scss']
})
export class SearchBarComponent implements OnInit {

  searchForm: FormGroup;

  planets: Planet[] = [];
  startPlanet: string;
  finishPlanet: string;
  spaceportsFrom: Spaceport[] = [];
  spaceportsTo: Spaceport[] = [];

  minimumDate = new Date();
  minFinishDate = new Date();

  field = '';
  
  @Output() onGetTripsNotifier = new EventEmitter();

  constructor(private landingService: LandingService,
             private router: Router,
             private dataService: DataService,
             private location: Location) {
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
    
    if(!this.location.isCurrentPathEqualTo('/')){
      
      this.dataService.getMessage().subscribe(message => { 
        this.searchForm.patchValue(message);
      })

    }else{
      this.getPlanets();
    }
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
    if(this.location.isCurrentPathEqualTo('/')){
      this.dataService.sendFormData(this.searchForm.value);
      this.router.navigate(['/trip-search']);
    }else{
      
      this.onGetTripsNotifier.emit(this.searchForm.value);
    }
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
