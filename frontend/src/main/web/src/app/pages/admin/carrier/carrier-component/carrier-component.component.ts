import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Carrier} from '../shared/model/carrier.model';
import {CarrierCrudService} from '../shared/service/carrier-crud.service';
import { clone } from 'ramda';
import {checkToken} from "../../../../modules/api/index";
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'app-carrier-component',
  templateUrl: './carrier-component.component.html',
  styleUrls: ['./carrier-component.component.scss']
})
export class CarrierComponentComponent implements OnInit {

  carPerPage = 10;
  passwordMinLength = 6;
  usernameMinLength = 3;
  usernameMaxLength = 24;

  carriers;
  allCarriers;
  currentCarrierForUpdate: Carrier;
  addBut: boolean = false;
  curPage: number = 0;

  butGroup = {
    editBut: false,
    updateBut: false,
    deleteBut: false
  };

  filterContent = '';
  form: FormGroup;

  dataAvailable: boolean = false;

  errorText:string = '';
  updateError = '';

  constructor(private carCrudService: CarrierCrudService) {

  }

  ngOnInit(): void {
    this.getCarriersPagin();

    this.createNewForm();
  }

  processUpdateEvent(event) {
    this.butGroup.updateBut = true;
    this.butGroup.editBut = true;
    this.updateError = '';
    this.currentCarrierForUpdate = event;
    this.carCrudService.updateCarrier(event)
                      .subscribe(
                        (resp: HttpResponse<any>) => {
                          checkToken(resp.headers);
                          this.butGroup.updateBut = false;
                          this.butGroup.editBut = false;
                          this.getCarriersPagin();
                         },
                        error => {
                          if(error.error.message = 'Username already exist'){
                            this.updateError = 'The user with this name alredy exists';
                          }else{
                            this.updateError = 'The user with this email alredy exists';
                          }
                        }
                      );
  }

  processDeleteNotification(event) {
    this.butGroup.deleteBut = true;
    this.butGroup.editBut = true;
    this.carCrudService.deleteCarrier(event)
                      .subscribe(
                        (resp: HttpResponse<any>) => {
                          checkToken(resp.headers);
                          this.butGroup.deleteBut = false;
                          this.butGroup.editBut = false;
                          this.getCarriersPagin();
                         },
                        error => {
                          this.butGroup.deleteBut = false;
                          this.butGroup.editBut = false;
                        }
                      );

  }

  processChangePage(event){
    this.curPage = event;
    this.getCarriersPagin();
  }

  getCarriersPagin(){
    this.carCrudService.getCarriersPagin(this.curPage, this.carPerPage)
                      .subscribe(
                        (resp: HttpResponse<any>) => {
                          checkToken(resp.headers);
                          this.carriers = clone(resp.body);
                          this.dataAvailable = true;
                          window.scrollTo(0, 0);
                         }
                      );
   }

   getAddingUser(){
     let status: boolean;
     status = this.form.value['status'] == 'activated';
     let newUser = {username: this.form.value['name'],
                password: '1111111',
                email: this.form.value['email'],
                telephone_number: this.form.value['tel'],
                is_activated: status};
      return newUser;
  }

  addCarrier(){
    this.addBut = true;
    let newCar = this.form.value;
    this.carCrudService.addCarrier(newCar)
                      .subscribe(
                        (resp: HttpResponse<any>) => {
                          checkToken(resp.headers);
                          this.getCarriersPagin();
                          this.clearform();
                         },
                        error => {
                          this.addBut = false;
                          if(error.error.message = 'Username already exist'){
                            this.errorText = 'The user with this name alredy exists';
                          }else{
                            this.errorText = 'The user with this email alredy exists';
                          }
                        }
                      );
  }

  createNewForm(){
    this.form = new FormGroup(
      {
        email: new FormControl('', [Validators.required, Validators.email]),
        username: new FormControl('', [Validators.required, Validators.minLength(this.usernameMinLength), Validators.maxLength(this.usernameMaxLength)]),
        telephone_number: new FormControl('', Validators.required),
        password: new FormControl('', [Validators.required, Validators.minLength(this.passwordMinLength)]),
        is_activated: new FormControl(true, Validators.required)
      }
    );
  }

  clearform(){
    this.errorText = '';
    this.form.reset();
    this.createNewForm();
    this.addBut = false;
  }

}
