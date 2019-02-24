import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Carrier} from '../carrier';
import {AddCarrier} from '../addCarrier';
import {CarrierCrudService} from '../carrier-crud.service';
import { clone } from 'ramda';

@Component({
  selector: 'app-carrier-component',
  templateUrl: './carrier-component.component.html',
  styleUrls: ['./carrier-component.component.scss']
})
export class CarrierComponentComponent implements OnInit {

  carPerPage = 10;

  carriers;
  allCarriers;
  currentCarrierForUpdate: Carrier;
  addBut: boolean = false;
  curPage: number = 1;

  filterCriteria = [
    {name: 'id'},
    {name: 'name'},
    {name: 'status'},
  ];

  butGroup = {
    editBut: false,
    updateBut: false,
    deleteBut: false
  };

  filterContent = '';

  currentFilter = this.filterCriteria[0].name;
  currentFilterPlaceholder = `Search by ${this.currentFilter}`;

  form: FormGroup;

  dataAvailable: boolean = false;

  constructor(private carCrudService: CarrierCrudService) {

  }

  ngOnInit(): void {

    this.getCarriers();
    this.getCarriersPagin();

    this.createNewForm();
  }

  chooseNewFilter(chosenFilterName) {

    this.currentFilter = chosenFilterName.value;

    this.currentFilterPlaceholder = `Search by ${this.currentFilter}`;
  }

  processUpdateEvent(event) {
    this.butGroup.updateBut = true;
    this.butGroup.editBut = true;
    this.currentCarrierForUpdate = event;
    this.carCrudService.updateCarrier(event)
                      .subscribe(
                        (resp: Response) => {
                          /*if (resp.headers.get('New-Access-Token')) {
                            localStorage.removeItem('at');
                            localStorage.setItem('at', resp.headers.get('New-Access-Token'));
                          }*/
                          console.log('carrier is updated');
                          this.butGroup.updateBut = false;
                          this.butGroup.editBut = false;
                          this.getCarriersPagin();
                         },
                        error => {
                          console.log(error);
                          this.butGroup.updateBut = false;
                          this.butGroup.editBut = false;
                        }
                      );
  }

  processDeleteNotification(event) {
    this.butGroup.deleteBut = true;
    this.butGroup.editBut = true;
    this.carCrudService.deleteCarrier(event)
                      .subscribe(
                        (resp: Response) => {
                          /*if (resp.headers.get('New-Access-Token')) {
                            localStorage.removeItem('at');
                            localStorage.setItem('at', resp.headers.get('New-Access-Token'));
                          }*/
                          console.log('carrier is deleted');
                          this.butGroup.deleteBut = false;
                          this.butGroup.editBut = false;
                          this.getCarriers();
                          this.getCarriersPagin();
                         },
                        error => {
                          console.log(error);
                          this.butGroup.deleteBut = false;
                          this.butGroup.editBut = false;
                        }
                      );

  }

  processChangePage(event){
    this.curPage = event;
    this.getCarriersPagin();
  }

  onSubmit() {
    this.addBut = true;
    let newUser = this.getAddingUser();
    this.addCarrier(newUser);
  }

  getCarriers(){
    this.carCrudService.getAllCarriers()
                      .subscribe(
                        (resp: Response) => {
                          /*if (resp.headers.get('New-Access-Token')) {
                            localStorage.removeItem('at');
                            localStorage.setItem('at', resp.headers.get('New-Access-Token'));
                          }*/
                          this.allCarriers = clone(resp);
                        },
                        error => console.log(error)
                      );
  }

  getCarriersPagin(){
    let from = (this.curPage*this.carPerPage)-(this.carPerPage-1);
    let to = this.curPage*this.carPerPage;
    console.log(from + ',' + to)
    this.carCrudService.getCarriersPagin(from, to)
                      .subscribe(
                        (resp: Response) => {
                          /*if (resp.headers.get('New-Access-Token')) {
                            localStorage.removeItem('at');
                            localStorage.setItem('at', resp.headers.get('New-Access-Token'));
                          }*/
                          this.carriers = clone(resp);
                          this.dataAvailable = true;
                         },
                        error => console.log(error)
                      );
   }

   getAddingUser(){
     let newUser: AddCarrier;
     let status: boolean;
     status = this.form.value['status'] == 'activated';
     newUser = {username: this.form.value['name'],
                password: '1111111',
                email: this.form.value['email'],
                telephone_number: this.form.value['tel'],
                is_activated: status};
      return newUser;
  }

  addCarrier(carrier: AddCarrier){
    this.carCrudService.addCarrier(carrier)
                      .subscribe(
                        (resp: Response) => {
                          /*if (resp.headers.get('New-Access-Token')) {
                            localStorage.removeItem('at');
                            localStorage.setItem('at', resp.headers.get('New-Access-Token'));
                          }*/
                          this.getCarriers();
                          this.getCarriersPagin();
                          this.clearform();
                          console.log('carrier is added');
                         },
                        error => {
                          this.clearform();
                          console.log(error);
                        }
                      );
  }

  createNewForm(){
    this.form = new FormGroup(
      {
        email: new FormControl('', [Validators.required, Validators.email]),
        name: new FormControl('', Validators.required),
        tel: new FormControl('', [Validators.required, Validators.pattern('[0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]')]),
        status: new FormControl('activated')
      }
    );
  }

  clearform(){
    this.form.reset();
    this.createNewForm();
    this.addBut = false;
  }

}
