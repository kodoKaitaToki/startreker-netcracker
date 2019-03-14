import {Component, OnInit, ViewChild } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Bundle} from '../shared/model/bundle';
import {BundlesService} from "../shared/service/bundles.service";
import {BundlesTableComponent} from "../bundles-table/bundles-table.component";
import {MessageService} from "primeng/api";
import {TreeNode} from 'primeng/api';
import {MOCK_TREE} from '../shared/model/mock-tree';
import {Carrier} from '../../carrier/carrier';
import {CarrierCrudService} from '../../carrier/carrier-crud.service';
import { TripsService } from '../../../../services/trips.service';
import {HttpErrorResponse} from "@angular/common/http";
import { Trip } from '../shared/model/trip';
import { TicketClass } from '../shared/model/ticket-class';

@Component({
             selector: 'app-bundles-component',
             templateUrl: './bundles-component.component.html',
             styleUrls: ['./bundles-component.component.scss']
           },
)
export class BundlesComponentComponent implements OnInit {
  @ViewChild(BundlesTableComponent) table:BundlesTableComponent;

  inputTree: TreeNode[];

  selectedTree: TreeNode[];

  filterCriteria = [
    {name: 'id'},
    {name: 'price'},
  ];

  filterContent = '';

  currentFilter = this.filterCriteria[0].name;

  currentFilterPlaceholder = `Search by ${this.currentFilter}`;

  form: FormGroup;

  constructor(
    private bundlesSrvc: BundlesService,
    private carrierSrvc: CarrierCrudService,
    private tripsApi: TripsService,
    private messageService: MessageService) {
  }

  ngOnInit(): void {
    this.populateCarriers();
    this.form = new FormGroup(
      {
        start_date: new FormControl('', Validators.required),
        finish_date: new FormControl('', Validators.required),
        price: new FormControl('', [Validators.required, Validators.min(0)]),
        description: new FormControl(''),
        trips: new FormControl(''),
        services: new FormControl('')
      }
    );
  }

  chooseNewFilter(chosenFilterName: { value: string; }) {

    this.currentFilter = chosenFilterName.value;
    this.currentFilterPlaceholder = `Search by ${this.currentFilter}`;
  }

  onPost() {

    let bundle: Bundle = this.form.value;

    //delete bundles['repeat_password'];

    this.bundlesSrvc.postBundles(bundle)
        .subscribe(() => {
          this.table.getAllBundles();
        }, () => {
          alert('Something went wrong');
        });

    this.form.reset({is_activated: true});
  }

  populateCarriers() {
    this.inputTree = [];
    this.carrierSrvc.getAllCarriers()
    .subscribe(carriers => {
      this.inputTree = this.inputTree.concat(
        carriers.map((carrier: Carrier) => {
          let node: TreeNode = {
            label: carrier.username,
            data: carrier.id,
            selectable: false,
            type: "carrier",
            leaf: false
          }
          return node;
      }))
    }, (error: HttpErrorResponse) => {
      this.showMessage(this.createMessage('error', `Error message - ${error.error.status}`, error.error.error));
    });
  }

  loadNode(event: { node: TreeNode; }) {
    if(event.node && event.node.type == "carrier") {
      switch(event.node.type) {
        case "carrier": {
          this.populateCarrier(event.node);
          break;
        }
      }
    }
  }

  populateCarrier(carrier: TreeNode) {
    this.tripsApi.getTripsForCarrier(carrier.data)
    .subscribe(trips => {
      carrier.children = trips.filter((trip: Trip) => {
        return (trip.trip_status == "Published")
      })
      .map((trip: Trip) => {
          let node: TreeNode = {
            label: trip.departure_planet + " - " + trip.arrival_planet,
            data: trip.trip_id,
            type: "trip",
            leaf: false,
            children: trip.ticket_classes.map((ticketClass: TicketClass) => {
              let node: TreeNode = {
                label: ticketClass.class_name,
                data: ticketClass.class_id,
                type: "ticketClass",
                leaf: false,
              }
              return node;
            })
          }
          return node;
      })
    }, (error: HttpErrorResponse) => {
      this.showMessage(this.createMessage('error', `Error message - ${error.error.status}`, error.error.error));
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
