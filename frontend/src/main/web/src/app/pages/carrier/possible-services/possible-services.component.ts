import { Component, OnInit } from '@angular/core';
import {clone} from 'ramda';
import { Trip } from '../trips/shared/model/trip';
import { TripsService } from '../../../services/trips.service';
import { TripTransfer } from '../trip-transfer';
import { ServiceService } from '../services/shared/service/service.service';
import { MessageService } from 'primeng/api';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { checkToken } from 'src/app/modules/api';
import { Service } from '../services/shared/model/service.model';
import { TicketClass } from '../../admin/bundles/shared/model/ticket-class';
import { Router } from '@angular/router';
import { PossibleServiceService } from './shared/services/possible-service.service';
import { FormBuilder, FormGroup, Validators, FormArray, FormControl, AbstractControl } from '@angular/forms';
import { registerNgModuleType } from '@angular/core/src/linker/ng_module_factory_loader';
import { PossibleService } from '../suggestions/shared/model/possible-service';

@Component({
  selector: 'app-possible-services',
  templateUrl: './possible-services.component.html',
  styleUrls: ['./possible-services.component.scss']
})
export class PossibleServicesComponent implements OnInit {

  readonly buisnessClassName: string = "BUISNESS";
  readonly firstClassName: string = "FIRST";
  readonly economyClassName: string = "ECONOMY";

  currentClassName: string;

  trip: Trip;

  buisnessClass: TicketClass;
  firstClass: TicketClass;
  economyClass: TicketClass;

  services: Service[];

  form: FormGroup =  this.fb.group(
    {
      possibleServices: this.fb.array([])
    }
    /*, {validator: Validators.compose([
      periodError("start_date", "finish_date"),
      emptyTable])}*/
  );

  constructor(
    private tripTransfer: TripTransfer,
    private serviceService: ServiceService,
    private possibleServiceService: PossibleServiceService,
    private messageService: MessageService,
    private fb: FormBuilder,
    private router: Router) { 
      
    }

  ngOnInit() {
    this.getTrip();

    this.getApprovedServices();

    this.currentClassName = this.economyClassName;
    let currentTicketClass = this.getCurrentTicketClass();

    this.getPossibleServices(currentTicketClass);
  }

  setClassName(ticketClass: string) {
    this.currentClassName = ticketClass;
    let currentTicketClass = this.getCurrentTicketClass();
    this.getPossibleServices(currentTicketClass);
  }

  getTrip() {
    this.trip = this.tripTransfer.storage;
    if (this.trip === undefined) {
      this.router.navigate(["carrier/trips"]);
    }

    this.economyClass = <TicketClass>this.trip.ticket_classes.find((ticketClass: TicketClass) => {
      return ticketClass.class_name == "economy";
    });
    this.firstClass = <TicketClass>this.trip.ticket_classes.find((ticketClass: TicketClass) => {
      return ticketClass.class_name == "first";
    });
    this.buisnessClass = <TicketClass>this.trip.ticket_classes.find((ticketClass: TicketClass) => {
      return ticketClass.class_name == "buisness";
    });

    let currentTicketClass = this.getCurrentTicketClass();
    console.log("currentTicketClass");
    console.log(currentTicketClass);
    this.getPossibleServices(currentTicketClass);
  }

  getCurrentTicketClass() : TicketClass {
    switch (this.currentClassName) {
      case (this.economyClassName):
        return this.economyClass;
      case (this.firstClassName):
        return this.firstClass;
      case (this.buisnessClassName):
        return this.buisnessClass;
      default:
        return undefined;
    }
  }

  getApprovedServices(){
    this.serviceService.getServiceByStatus('PUBLISHED', 0, 10)
        .subscribe((resp: HttpResponse<any>) => {
                      checkToken(resp.headers);
                      this.services = clone(resp.body);
                      this.showMessage(this.createMessage('success', 'service list', 'The list was updated'));
                  }, (error: HttpErrorResponse) => {
                    this.showMessage(this.createMessage('error', `Error message - ${error.error.status}`, error.error.message));
                  });
  }

  getPossibleServices(ticketClass: TicketClass) {
    if(ticketClass === undefined) {
      return;
    }
    let id = ticketClass.class_id;
    this.possibleServiceService.getPossibleServices(id)
    .subscribe((resp: HttpResponse<any>) => {
      checkToken(resp.headers);
      this.loadServices(clone(resp.body));
      this.showMessage(this.createMessage('success', 'service list', 'The list was updated'));
    }, (error: HttpErrorResponse) => {
      let control = <FormArray>this.form.controls.possibleServices;
      while (control.length !== 0) {
        control.removeAt(0);
      }
      this.showMessage(this.createMessage('error', `Error message - ${error.error.status}`, error.error.message));
    });
  }

  addServiceToForm(service: Service) {
    let control = <FormArray>this.form.controls.possibleServices;

    console.log(control.value);

    let exists:boolean = control.controls.some((c: AbstractControl) => {
      return c.get("service_id").value == service.id;
    });

    if (exists) {
      this.showMessage(this.createMessage('error', `Service already added`, "Service is already in possible services"));
      return;
    }

    control.push(this.fb.group({
      id: [''],
      service_id: service.id,
      service_name: service.service_name,
      service_description: service.service_descr,
      class_id: this.getCurrentTicketClass().class_id,
      service_price: ['', Validators.required]
    }));
  }

  loadServices(services: PossibleService[]) {
    let control = <FormArray>this.form.controls.possibleServices;

    while (control.length !== 0) {
      control.removeAt(0);
    }

    services.forEach(service => {
      this.loadServiceToForm(service);
    })
  }

  loadServiceToForm(service: PossibleService) {
    let control = <FormArray>this.form.controls.possibleServices;

    control.push(this.fb.group({
      id: service.id,
      service_id: service.service_id,
      service_name: service.service_name,
      service_description: service.service_description,
      class_id: service.class_id,
      service_price: service.service_price
    }));
  }

  onSave(control: AbstractControl) {
    console.log("debug: control value is");
    console.log(control);
    console.log(control.get("id"));
    let emptyId:boolean = control.get("id").value == '';

    if (!emptyId) {
      this.onPut(control);
    }
    else {
      this.onPost(control);
    }
  }

  onPost(control: AbstractControl) {
    this.possibleServiceService.createPossibleService(control.value)
    .subscribe((resp: HttpResponse<any>) => {
      checkToken(resp.headers);
      this.showMessage(this.createMessage('success', 'service creation', 'The possible service was created'));
      let currentTicketClass = this.getCurrentTicketClass();
      this.getPossibleServices(currentTicketClass);
    }, (error: HttpErrorResponse) => {
      this.showMessage(this.createMessage('error', `Error message - ${error.error.status}`, error.error.message));
    });
  }

  onPut(control: AbstractControl) {
    this.possibleServiceService.updatePossibleService(control.value)
    .subscribe((resp: HttpResponse<any>) => {
      checkToken(resp.headers);
      this.showMessage(this.createMessage('success', 'service update', 'The possible service was updated'));
      let currentTicketClass = this.getCurrentTicketClass();
      this.getPossibleServices(currentTicketClass);
    }, (error: HttpErrorResponse) => {
      this.showMessage(this.createMessage('error', `Error message - ${error.error.status}`, error.error.message));
    });
  }

  onDelete(control: AbstractControl) {
    this.possibleServiceService.deletePossibleService(control.get("id").value)
    .subscribe((resp: HttpResponse<any>) => {
      checkToken(resp.headers);
      this.showMessage(this.createMessage('success', 'service deletion', 'The possible service was deleted'));
      let currentTicketClass = this.getCurrentTicketClass();
      this.getPossibleServices(currentTicketClass);
    }, (error: HttpErrorResponse) => {
      this.showMessage(this.createMessage('error', `Error message - ${error.error.status}`, error.error.message));
    });
  }

  createMessage(severity: string, summary: string, detail: string): any {
    return {
      severity: severity,
      summary: summary,
      detail: detail
    };
  }

  showMessage(msgObj: any) {
    this.messageService.add(msgObj);
  }

}

