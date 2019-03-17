import {Component, EventEmitter, Input, OnInit, Output, ViewChildren, QueryList} from '@angular/core';
import {Bundle} from '../shared/model/bundle';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {BundlesService} from "../shared/service/bundles.service";
import {MessageService} from "primeng/api";
import {HttpErrorResponse} from "@angular/common/http";
import {Carrier} from '../../carrier/carrier';
import {CarrierCrudService} from '../../carrier/carrier-crud.service';
import { Trip } from '../shared/model/trip';
import { TicketClass } from '../shared/model/ticket-class';
import { Service } from '../shared/model/service';
import { BundlesFormComponent } from '../bundles-form/bundles-form.component';


@Component({
             selector: 'app-bundles-table',
             templateUrl: './bundles-table.component.html',
             styleUrls: ['./bundles-table.component.scss']
           })
export class BundlesTableComponent implements OnInit {
  @ViewChildren(BundlesFormComponent) forms !: QueryList<BundlesFormComponent>;

  readonly pageNumber: number = 10;

  pageFrom: number;

  pageAmount: number;

  bundles: Bundle[];

  currentBundleForUpdate: Bundle;

  currentBundleToHideId: number;

  constructor(
    private bundlesSrvc: BundlesService,
    private messageService: MessageService,) {

  }

  ngOnInit() {
    this.pageFrom = 0;

    this.getAllBundles();
  }

  onBundleUpdate(onClickedBundleForUpdate) {
    this.currentBundleForUpdate = onClickedBundleForUpdate;
    let bundleFormComponent: BundlesFormComponent;
    bundleFormComponent =  this.forms.find(b => {
      return b.bundleId == this.currentBundleForUpdate.id;
    });
    bundleFormComponent.onBundleUpdate(this.currentBundleForUpdate);
    //bundleFormComponent.onBundleUpdate(this.currentBundleForUpdate);
  }

  cancelUpdate() {
    this.currentBundleForUpdate = undefined;
  }

  onPut(bundle) {
    this.currentBundleToHideId = bundle.id;
    console.log("Putting bundle");
    console.log(bundle);
    this.bundlesSrvc.putBundle(bundle)
    .subscribe(() => {
      this.showMessage(this.createMessage('success', 'Bundle editing', 'The bundle was updated'));
      this.getAllBundles();
    }, (error: HttpErrorResponse) => {
      this.showMessage(this.createMessage('error', `Error message - ${error.error.status}`, error.error.error));
      this.currentBundleToHideId = undefined;
    });
  }

  onDelete(bundle) {
    this.currentBundleToHideId = bundle.id;
    this.bundlesSrvc.deleteBundle(bundle)
    .subscribe(() => {
      this.showMessage(this.createMessage('success', 'Bundle deletion', 'The bundle was deleted'));
      this.getAllBundles();
    }, (error: HttpErrorResponse) => {
      this.showMessage(this.createMessage('error', `Error message - ${error.error.status}`, error.error.error));
      this.currentBundleToHideId = undefined;
    });
  }

  getAllBundles() {
    this.bundlesSrvc.getCount()
        .subscribe(data => this.pageAmount = data);

    this.bundlesSrvc.getBundlesInInterval(this.pageNumber, this.pageFrom)
        .subscribe(data => {
          this.showMessage(this.createMessage('success', 'Bundle list', 'The list was updated'));
          this.bundles = data;
          this.currentBundleToHideId = undefined;
          this.currentBundleForUpdate = undefined;
        }, (error: HttpErrorResponse) => {
          this.showMessage(this.createMessage('error', `Error message - ${error.error.status}`, error.error.error));
        })
  }

/*  checkRepeatedPasswordTheSame() {
    return this.form.get('password').value === this.form.get('repeat_password').value;
  } */

  onPageUpdate(from: number) {
    this.pageFrom = from;
    this.getAllBundles();
  }

  createMessage(severity: string, summary: string, detail: string): any {
    return {
      severity: severity,
      summary: summary,
      detail: detail
    };
  }

  getBundlePrice(bundle: Bundle) {
    let tripsPrice: number = bundle.bundle_trips
    .map(trip => this.getTripPrice(trip))
    .reduce(( acc, cur ) => acc + cur, 0);
    return (tripsPrice);
  }

  getTripPrice(trip: Trip) {
    let classesPrice: number = trip.ticket_classes
    .map(ticketClass => this.getTicketPrice(ticketClass))
    .reduce(( acc, cur ) => acc + cur, 0);
    return (classesPrice);
  }

  getTicketPrice(ticketClass: TicketClass) {
    let servicesPrice: number = ticketClass.services
    .map(service => this.getServicePrice(service))
    .reduce(( acc, cur ) => acc + cur, 0);
    return ((ticketClass.ticket_price || 0) * (ticketClass.item_number || 0) + servicesPrice);
  }

  getServicePrice(service: Service) {
    return ((service.service_price || 0) * (service.item_number || 0));
  }

  showMessage(msgObj: any) {
    this.messageService.add(msgObj);
  }
}
