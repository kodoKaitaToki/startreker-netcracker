import { BehaviorSubject, Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Planet } from '../pages/landing/shared/model/planet.model';
import { Spaceport } from 'src/app/pages/landing/shared/model/spaceport.model';

@Injectable({ providedIn: 'root' })
export class DataService {

  private formData = new BehaviorSubject<FormGroup>(new FormGroup({}));
  private planets = new  BehaviorSubject<Planet[]>([]);
  private spaceports = new  BehaviorSubject<{startPorts: Spaceport[],
                                             finishPorts: Spaceport[]}>({startPorts : [],
                                                                        finishPorts : []});

  constructor() { }

  sendFormData(formData) {
    this.formData.next(formData);
  }

  sendPlanetsData(planets: Planet[]){
    this.planets.next(planets);
  }

  sendSpaceportsData(ports: {startPorts: Spaceport[],
                            finishPorts: Spaceport[]}){
    this.spaceports.next(ports);
  }

  getMessage(): Observable<FormGroup> {
    return this.formData.asObservable();
  }

  getPlanets(): Observable<Planet[]>{
    return this.planets.asObservable();
  }

  getSpaceports(): Observable<{startPorts: Spaceport[],
                              finishPorts: Spaceport[]}>{
    return this.spaceports.asObservable();
  }
}