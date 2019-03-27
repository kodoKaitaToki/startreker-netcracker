import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Carrier } from '../shared/model/carrier.model';
import { CarrierCrudService } from '../shared/service/carrier-crud.service';
import { clone } from 'ramda';
import { checkToken } from "../../../../modules/api/index";
import { HttpResponse } from '@angular/common/http';
import { MessageService } from "primeng/api";
import { ShowMessageService } from '../../approver/shared/service/show-message.service';

@Component({
  selector: 'app-carrier-component',
  templateUrl: './carrier-component.component.html',
  styleUrls: ['./carrier-component.component.scss'],
  providers: [ShowMessageService]
})
export class CarrierComponentComponent implements OnInit {

  carPerPage = 10;
  passwordMinLength = 6;
  usernameMinLength = 3;
  usernameMaxLength = 24;

  carriers: Carrier[] = [];
  currentCarrierForUpdate: Carrier;
  pageFrom: number = 0;
  carrierNumber: number = 0;

  butGroup = {
    editBut: false,
    updateBut: false,
    deleteBut: false,
    addBut: false,
    cancelBut: false
  };

  filterContent = '';
  form: FormGroup;
  editForm: FormGroup;

  constructor(private carCrudService: CarrierCrudService,
              private messageService: MessageService,
              private showMsgSrvc: ShowMessageService) {}

  ngOnInit(): void {
    this.getCarriersPagin();
    this.getCarrierNumber();
    this.createNewForm();
  }

  onDelete(id: number) {
    this.butGroup.deleteBut = true;
    this.butGroup.editBut = true;
    this.carCrudService.deleteCarrier(id)
                      .subscribe(
                        (resp: HttpResponse<any>) => {
                          checkToken(resp.headers);
                          this.butGroup.deleteBut = false;
                          this.butGroup.editBut = false;
                          this.getCarriersPagin();
                          this.showMsgSrvc.showMessage(this.messageService, 
                            'success', 
                            'Success', 
                            'The carrier was deleted');
                         },
                        error => {
                          this.butGroup.deleteBut = false;
                          this.butGroup.editBut = false;
                          this.showMsgSrvc.showMessage(this.messageService, 
                            'error', 
                            'Error', 
                            error.error.message);
                        }
                      );

  }

  onSubmit() {
    this.butGroup.updateBut = true;
    this.carCrudService.updateCarrier(this.editForm.value, this.currentCarrierForUpdate.id)
                      .subscribe(
                        (resp: HttpResponse<any>) => {
                          checkToken(resp.headers);
                          this.butGroup.updateBut = false;
                          this.butGroup.editBut = false;
                          this.getCarriersPagin();
                          this.showMsgSrvc.showMessage(this.messageService, 
                                                      'success', 
                                                      'Success', 
                                                      'The carrier was updated');
                         },
                        error => {
                          this.butGroup.updateBut = false;
                          this.showMsgSrvc.showMessage(this.messageService, 
                                                      'error', 
                                                      'Error', 
                                                      error.error.message);
                        }
                      );
  }

  onChange(event: number){
    this.pageFrom = event;
    window.scrollTo(0, 0);
    this.getCarriersPagin();
  }

  getCarriersPagin(){
    this.carCrudService.getCarriersPagin(this.pageFrom, this.carPerPage)
                      .subscribe(
                        (resp: HttpResponse<any>) => {
                          checkToken(resp.headers);
                          this.carriers = clone(resp.body);
                          window.scrollTo(0, 0);
                         }
                      );
   }

   getCarrierNumber(){
    this.carCrudService.getCarrierNumber().subscribe((response: number) => this.carrierNumber = response);
  }

  addCarrier(){
    this.butGroup.addBut = true;
    this.carCrudService.addCarrier(this.form.value)
                      .subscribe(
                        (resp: HttpResponse<any>) => {
                          checkToken(resp.headers);
                          this.getCarriersPagin();
                          this.form.reset({is_activated: true});
                          this.butGroup.addBut = false;
                          this.carrierNumber += 1;
                          this.showMsgSrvc.showMessage(this.messageService, 
                                                      'success', 
                                                      'New carrier', 
                                                      'The new carrier was added');
                         },
                        error => {
                          this.butGroup.addBut = false;
                          this.showMsgSrvc.showMessage(this.messageService, 
                                                      'error', 
                                                      'Error', 
                                                      error.error.message);
                        }
                      );
  }

  onUpdate(event) {
    this.currentCarrierForUpdate = event;
    this.editForm.patchValue(event);
    this.butGroup.editBut = true;
  }

  createNewForm(){
    this.form = new FormGroup(
      {
        email: new FormControl('', [Validators.required, Validators.email]),
        username: new FormControl('', [Validators.required, Validators.minLength(this.usernameMinLength), Validators.maxLength(this.usernameMaxLength)]),
        telephone_number: new FormControl('', [Validators.required, Validators.pattern("[0-9]{10}")]),
        password: new FormControl('', [Validators.required, Validators.minLength(this.passwordMinLength)]),
        is_activated: new FormControl(true, Validators.required)
      }
    );

    this.editForm = new FormGroup(
      {
        email: new FormControl('', [Validators.required, Validators.email]),
        username: new FormControl('', [Validators.required, Validators.minLength(this.usernameMinLength), Validators.maxLength(this.usernameMaxLength)]),
        telephone_number: new FormControl('', [Validators.required, Validators.pattern("[0-9]{10}")]),
        is_activated: new FormControl(true, Validators.required)
      }
    );
  }
}
