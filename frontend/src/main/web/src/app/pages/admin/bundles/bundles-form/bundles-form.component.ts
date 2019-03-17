import { Component, OnInit, ViewChild, Input, EventEmitter, Output } from '@angular/core';
import { FormArray, Validators, FormBuilder, FormGroup } from '@angular/forms';
import { Trip } from '../shared/model/trip';
import { TicketClass } from '../shared/model/ticket-class';
import { Bundle } from '../shared/model/bundle';
import { Service } from '../shared/model/service';
import { BundleForm } from '../shared/model/bundle-form';
import { HttpErrorResponse } from '@angular/common/http';
import { BundlesService } from '../shared/service/bundles.service';
import { MessageService } from 'primeng/api';
import { BundlesTreeComponent } from '../bundles-tree/bundles-tree.component';
import { DatePipe, formatDate } from '@angular/common';

@Component({
  selector: 'app-bundles-form',
  templateUrl: './bundles-form.component.html',
  styleUrls: ['./bundles-form.component.scss']
})
export class BundlesFormComponent implements OnInit {
  @Input() bundleId: number;

  @ViewChild(BundlesTreeComponent) tree:BundlesTreeComponent;

  selectedTrips: Trip[];

  @Output() bundleForm = new EventEmitter<BundleForm>();

  form: FormGroup =  this.fb.group(
    {
      id: [''],
      start_date: ['', Validators.required],
      finish_date: ['', Validators.required],
      bundle_price: ['', [Validators.required, Validators.min(0)]],
      bundle_description: ['', Validators.required],
      photo_uri: [''],
      bundle_trips: this.fb.array([])
    }
  );

  constructor(
    private fb: FormBuilder,
    public datepipe: DatePipe) {
  }

  ngOnInit() {
    this.selectedTrips = [];
  }

  onBundleUpdate(bundle: Bundle) {
    this.form.patchValue({
      id: bundle.id,
      start_date: bundle.start_date,
      finish_date: bundle.finish_date,
      bundle_price: bundle.bundle_price,
      bundle_description: bundle.bundle_description,
    });
    this.tripsUpdate(bundle.bundle_trips);
  }

  onPost() {

    let bundle: Bundle = this.form.value;

    console.log(this.prepareForPost(bundle));

    this.bundleForm.emit(this.prepareForPost(bundle));

    this.cleanForm();
  }

  prepareForPost(bundle: Bundle) : BundleForm {
    let bundleForm: BundleForm;
    bundleForm = {
      id: bundle.id,
      start_date: `${this.datepipe.transform(bundle.start_date, 'yyyy-MM-dd HH:mm')}`,
      finish_date: `${this.datepipe.transform(bundle.start_date, 'yyyy-MM-dd HH:mm')}`,
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
        item_number: [ticletClass.item_number, [Validators.required, Validators.min(0)]],
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
        item_number: [service.item_number, [Validators.required, Validators.min(0)]]
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

}
