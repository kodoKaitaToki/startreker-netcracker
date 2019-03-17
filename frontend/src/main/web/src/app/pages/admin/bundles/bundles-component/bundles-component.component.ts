import {Component, OnInit, ViewChild } from '@angular/core';
import {FormControl, FormGroup, FormArray, Validators, FormBuilder} from '@angular/forms';
import {Bundle} from '../shared/model/bundle';
import {BundleForm} from '../shared/model/bundle-form';
import {BundlesService} from "../shared/service/bundles.service";
import {BundlesTableComponent} from "../bundles-table/bundles-table.component";
import {BundlesTreeComponent} from "../bundles-tree/bundles-tree.component";
import {MessageService} from "primeng/api";
import {TreeNode} from 'primeng/api';
import { Trip } from '../shared/model/trip';
import { TicketClass } from '../shared/model/ticket-class';
import { Service } from '../shared/model/service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
             selector: 'app-bundles-component',
             templateUrl: './bundles-component.component.html',
             styleUrls: ['./bundles-component.component.scss']
           },
)
export class BundlesComponentComponent implements OnInit {
  @ViewChild(BundlesTableComponent) table:BundlesTableComponent;
  @ViewChild(BundlesTreeComponent) tree:BundlesTreeComponent;

  selectedTrips: Trip[];

  form: FormGroup =  this.fb.group(
    {
      start_date: ['', Validators.required],
      finish_date: ['', Validators.required],
      bundle_price: ['', [Validators.required, Validators.min(0)]],
      bundle_description: ['', Validators.required],
      photo_uri: [''],
      bundle_trips: this.fb.array([])
    }
  );

  constructor(
    private bundlesSrvc: BundlesService,
    private fb: FormBuilder,
    private messageService: MessageService) {
  }

  ngOnInit(): void {
    this.selectedTrips = [];
  }

  onPost() {

    let bundle: Bundle = this.form.value;

    //delete bundles['repeat_password'];

    console.log(this.prepareForPost(bundle));

    this.bundlesSrvc.postBundles(this.prepareForPost(bundle))
        .subscribe(() => {
          this.showMessage(this.createMessage('success', 'Bundle post', 'The bundle was created'));
          this.table.getAllBundles();
        }, (error: HttpErrorResponse) => {
          this.showMessage(this.createMessage('error', `Error message - ${error.error.status}`, error.error.error));
        });

    this.cleanForm();
  }

  prepareForPost(bundle: Bundle) : BundleForm {
    let bundleForm: BundleForm;
    bundleForm = {
      id: bundle.id,
      start_date: `${bundle.start_date} 00:00`,
      finish_date: `${bundle.finish_date} 00:00`,
      bundle_price: bundle.bundle_price,
      bundle_description: bundle.bundle_description,
      photo_uri: "",
      bundle_trips: bundle.bundle_trips.map(trip => {
        return {
          trip_id: trip.trip_id,
          ticket_classes: trip.ticket_classes.map(ticketClass => {
            return {
              ticket_class_id: ticketClass.class_id,
              item_number: ticketClass.item_number,
              services: ticketClass.services.map(service => {
                return {
                  service_id: service.service_id,
                  item_number: service.item_number
                }
              })
            }
          })
        }
      })
    };
    return bundleForm;
  }

  cleanForm() {
    this.tree.selectedTree.length = 0;
    this.tree.bundleUpdate();
    this.form.reset({is_activated: true});
  }

  tripsUpdate(trips: Trip[]) {
    this.selectedTrips = trips;
    this.setTrips();
  }

  setTrips() {
    let control = <FormArray>this.form.controls.bundle_trips;

    while (control.length !== 0) {
      control.removeAt(0);
    }

    this.selectedTrips.forEach(trip => {
      control.push(this.fb.group({
        trip_id: trip.trip_id,
        departure_spaceport_name: trip.departure_spaceport_name,
        departure_planet: trip.departure_planet,
        arrival_spaceport_name: trip.arrival_spaceport_name,
        arrival_planet: trip.arrival_planet,
        ticket_classes: this.setTicketClasses(trip)
      }))
    });
  }

  setTicketClasses(trip: Trip) : FormArray {
    let arr = new FormArray([]);
    trip.ticket_classes.forEach(ticletClass => {
      arr.push(this.fb.group({
        class_id: ticletClass.class_id,
        class_name: ticletClass.class_name,
        ticket_price: ticletClass.ticket_price,
        item_number: ['', [Validators.required, Validators.min(0)]],
        services: this.setServices(ticletClass)
      }))
    });
    return arr;
  }

  setServices(ticketClass: TicketClass) : FormArray {
    let arr = new FormArray([]);
    ticketClass.services.forEach(service => {
      arr.push(this.fb.group({
        service_id: service.service_id,
        service_name: service.service_name,
        service_price: service.service_price,
        item_number: ['', [Validators.required, Validators.min(0)]]
      }))
    });
    return arr;
  }

  getBundlePrice(bundle: Bundle) {
    if (bundle.bundle_trips === undefined) {
      return 0;
    }
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
