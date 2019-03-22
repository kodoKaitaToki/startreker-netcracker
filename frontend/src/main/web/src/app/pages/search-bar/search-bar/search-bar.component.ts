import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { LandingService } from '../../landing/shared/service/landing.service';
import { Planet } from '../../landing/shared/model/planet.model';
import { FormGroup, FormControl, Validators } from '@angular/forms';
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

  @Output() onGetTripsNotifier = new EventEmitter();

  constructor(private landingService: LandingService,
             private router: Router,
             private dataService: DataService,
             private location: Location) {
    this.searchForm = new FormGroup({
      startPlanet: new FormControl({value: 'Choose a planet', disabled: true}, Validators.required),
      finishPlanet: new FormControl({value: 'Choose a planet', disabled: true}, Validators.required),
      startSpaceport: new FormControl({value: 'Choose a spaceport', disabled: true}, Validators.required),
      finishSpaceport: new FormControl({value: 'Choose a spaceport', disabled: true}, Validators.required),
      startDate: new FormControl('', Validators.required),
      finishDate: new FormControl({value: '', disabled: false})
    });
   }

  ngOnInit() {
    this.dataService.getPlanets().subscribe(message => {this.planets = message});
    this.dataService.getSpaceports().subscribe(message => {
      this.spaceportsFrom = message.startPorts;
      this.spaceportsTo = message.finishPorts;
    });
    if(!this.location.isCurrentPathEqualTo('/')){

      this.searchForm.get('startSpaceport').enable();
      this.searchForm.get('finishSpaceport').enable();
      this.enablePlanets();

      this.dataService.getMessage().subscribe(message => {       
        this.searchForm.patchValue(message);
      });

      this.onGetTripsNotifier.emit(this.searchForm.value);
    }else{
      this.getPlanets();
    }
  }

  getPlanets(){
    this.landingService.getPlanets()
        .subscribe((resp: Response) => {
          this.planets = clone(resp);
          this.enablePlanets();
        })
  }

  setPoint(point){
    setPoint(point);
  }

  onSubmit(){
    if(this.location.isCurrentPathEqualTo('/')){
      this.dataService.sendFormData(this.searchForm.value);
      this.dataService.sendPlanetsData(this.planets);
      this.dataService.sendSpaceportsData({startPorts: this.spaceportsFrom,
                                          finishPorts: this.spaceportsTo});
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

  onSelect(event){
    if(this.searchForm.get('finishDate').value < event){
      this.searchForm.patchValue({finishDate: ''});
    }
    this.minFinishDate.setDate(event.getDate() + 1);
  }

  enablePlanets(){
    this.searchForm.get('startPlanet').enable();
    this.searchForm.get('finishPlanet').enable();
  }

}
