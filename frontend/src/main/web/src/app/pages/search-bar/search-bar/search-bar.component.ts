import {Component, EventEmitter, OnDestroy, OnInit, Output} from '@angular/core';
import {LandingService} from '../../landing/shared/service/landing.service';
import {Planet} from '../../landing/shared/model/planet.model';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Spaceport} from 'src/app/pages/landing/shared/model/spaceport.model';
import {Router} from '@angular/router';
import {DataService} from '../../../shared/data.service';
import {clone} from 'ramda';
import {Location} from '@angular/common';
import {Subscription} from "rxjs/internal/Subscription";

declare function setPoint(point): any;

@Component({
  selector: 'app-search-bar',
  templateUrl: './search-bar.component.html',
  styleUrls: ['./search-bar.component.scss']
})
export class SearchBarComponent implements OnInit, OnDestroy {

  searchForm: FormGroup;

  planets: Planet[] = [];
  startPlanet: string;
  finishPlanet: string;
  spaceportsFrom: Spaceport[] = [];
  spaceportsTo: Spaceport[] = [];

  minimumDate = new Date();
  // minFinishDate = new Date();

  @Output() onGetTripsNotifier = new EventEmitter();

  dataServiceSub: Subscription;
  landingServiceSub: Subscription;

  constructor(private landingService: LandingService,
              private router: Router,
              private dataService: DataService,
              private location: Location) {
    this.searchForm = new FormGroup({
      startPlanet: new FormControl(Validators.required),
      finishPlanet: new FormControl(Validators.required),
      startSpaceport: new FormControl({value: '', disabled: true}),
      finishSpaceport: new FormControl({value: '', disabled: true}),
      startDate: new FormControl(new Date(), Validators.required)
      // finishDate: new FormControl({value: '', disabled: false})
    });
  }

  ngOnInit() {
    //this.enablePlanets();
    // this.searchForm.get('startSpaceport').enable();
    // this.searchForm.get('finishSpaceport').enable();
    this.dataServiceSub = this.dataService.getMessage().subscribe(message => {
      console.log("ngOnInit() message: " + JSON.stringify(message));
      console.log("ngOnInit() searchForm: " + JSON.stringify(this.searchForm.value));
      this.searchForm.patchValue(message);
    });
    this.getPlanets();
    console.log(this.searchForm.value);
    this.onGetTripsNotifier.emit(this.searchForm.value);
  }


  getPlanets() {
    this.dataServiceSub = this.landingService.getPlanets()
      .subscribe((resp: Response) => {
        this.planets = clone(resp);
        this.enablePlanets();
      })
  }

  checkPlanet(point){
    if(this.router.url !== '/flights'){
      setPoint(point);
    }
  }

  onSubmit() {
    if (this.location.isCurrentPathEqualTo('/')) {
      console.log(JSON.stringify(this.searchForm.value));
      this.dataService.sendFormData(this.searchForm.value);
      this.router.navigate(['/flights']);
    } else {
      this.onGetTripsNotifier.emit(this.searchForm.value);
    }
  }

  getSpaceports(point: boolean) {
    let planetName;
    if (point) {
      this.searchForm.get('startSpaceport').disable();
      planetName = this.searchForm.get('startPlanet').value;
    } else {
      this.searchForm.get('finishSpaceport').disable();
      planetName = this.searchForm.get('finishPlanet').value;
    }
    for (let planet of this.planets) {
      if (planet.planetName == planetName.toString().toUpperCase()) {
        this.landingServiceSub = this.landingService.getSpaceports(planet.planetId)
          .subscribe((resp: Response) => {
            if (point) {
              this.spaceportsFrom = clone(resp);
              this.searchForm.get('startSpaceport').enable();
            } else {
              this.spaceportsTo = clone(resp);
              this.searchForm.get('finishSpaceport').enable();
            }
          });
      }
    }

  }

  // onSelect(event) {
  //   if (this.searchForm.get('finishDate').value < event) {
  //     this.searchForm.patchValue({finishDate: ''});
  //   }
  //   this.minFinishDate.setDate(event.getDate() + 1);
  // }

  enablePlanets() {
    this.searchForm.get('startPlanet').enable();
    this.searchForm.get('finishPlanet').enable();
  }

  ngOnDestroy() {
    if (this.dataServiceSub !== undefined)
      this.dataServiceSub.unsubscribe();
    if (this.landingServiceSub !== undefined)
      this.landingServiceSub.unsubscribe();
  }
}
