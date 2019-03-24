import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { TreeNode, MessageService } from 'primeng/api';
import { Trip } from '../shared/model/trip';
import { CarrierCrudService } from '../../carrier/shared/service/carrier-crud.service';
import { TripsService } from 'src/app/services/trips.service';
import { PossibleServiceService } from 'src/app/services/possible-service.service';
import { Carrier } from '../../carrier/shared/model/carrier.model';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { TicketClass } from '../shared/model/ticket-class';
import { Service } from '../shared/model/service';
import { clone } from 'ramda';
import { checkToken } from 'src/app/modules/api';

@Component({
  selector: 'app-bundles-tree',
  templateUrl: './bundles-tree.component.html',
  styleUrls: ['./bundles-tree.component.scss']
})
export class BundlesTreeComponent implements OnInit {

  inputTree: TreeNode[];

  selectedTree: TreeNode[];

  loading: boolean;

  @Output() selectedTrips = new EventEmitter<Trip[]>();

  constructor(
    private carrierSrvc: CarrierCrudService,
    private tripsApi: TripsService,
    private possibleServiceApi: PossibleServiceService,
    private messageService: MessageService) {
  }

  ngOnInit() {
    this.getCarriers();
  }

  getCarriers() {
    this.loading = true;
    this.inputTree = [];
    this.carrierSrvc.getAllCarriers()
    .subscribe((resp: HttpResponse<any>) => {
      checkToken(resp.headers);
      let carriers = clone(resp.body);
      this.inputTree = this.inputTree.concat(
        carriers.map((carrier: Carrier) => {
          let node: TreeNode = {
            label: carrier.username,
            data: carrier,
            icon: "fa fa-building-o",
            selectable: false,
            type: "carrier",
            leaf: false
          }
          return node;
      }));
      this.loading = false;
    }, (error: HttpErrorResponse) => {
      this.showMessage(this.createMessage('error', `Error message - ${error.error.status}`, error.error.error));
    });
  }

  loadNode(event: { node: TreeNode; }) {
    let nullChecks:boolean = (this.selectedTree !== undefined 
      && event.node.children !== undefined);

    if(event.node) {
      if (nullChecks
        && event.node.children.some(node => this.selectedTree.includes(node))) {
        this.showMessage(this.createMessage('warn', `Nodes still selected`, 'Nodes will not load if any are selected'));
        return;
      }
      switch(event.node.type) {
        case "carrier": {
          this.populateCarrier(event.node);
          this.bundleUpdate();
          break;
        }
        case "ticketClass": {
          this.populateTicketClass(event.node);
          this.bundleUpdate();
          break;
        }
      }
    }
  }

  populateCarrier(carrier: TreeNode) {
    this.tripsApi.getTripsForCarrier(carrier.data.id)
    .subscribe((resp: HttpResponse<any>) => {
      checkToken(resp.headers);
      let trips = clone(resp.body);
      let properTrips = trips.map(trip => {
        let properTrip = trip;
        properTrip.departure_spaceport_name = properTrip.departure_spaceport;
        delete properTrip.departure_spaceport;
        properTrip.arrival_spaceport_name = properTrip.arrival_spaceport;
        delete properTrip.arrival_spaceport;
        return properTrip;
      });
      console.log(properTrips);
      carrier.children = properTrips.filter(trip => {
        return (trip.trip_status == "PUBLISHED")
      })
      .map(trip => {
          let node: TreeNode = {
            label: `${trip.departure_spaceport_name}(${trip.departure_planet}) -  ${trip.arrival_spaceport_name}(${trip.arrival_planet})`,
            data: trip,
            icon: "fa fa-rocket",
            type: "trip",
            leaf: false,
            children: trip.ticket_classes.map((ticketClass: TicketClass) => {
              let node: TreeNode = {
                label: ticketClass.class_name,
                data: ticketClass,
                icon: "fa fa-ticket",
                selectable: false,
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

  populateTicketClass(ticketClass: TreeNode) {
    this.possibleServiceApi.getAll(ticketClass.data.class_id)
    .subscribe((resp: HttpResponse<any>) => {
      checkToken(resp.headers);
      let services = clone(resp.body);
      ticketClass.children = services.map((service: Service) => {
          let node: TreeNode = {
            label: service.service_name,
            data: service,
            type: "service",
            icon: "fa fa-list-alt",
            selectable: false,
            leaf: true
          }
          return node;
      })
    }, (error: HttpErrorResponse) => {
      this.showMessage(this.createMessage('error', `Error message - ${error.error.status}`, error.error.message));
    });
  }

  nodeSelect(event: { node: TreeNode; }) {
    if (event.node.children !== undefined) {
      event.node.children
      .forEach((node: TreeNode) => node.selectable = true);
    }
    event.node.parent.selectable = false;
    event.node.parent.leaf = true;
    this.bundleUpdate();
  }

  nodeUnselect(event: { node: TreeNode; }) {
    if (event.node.children !== undefined) {
      event.node.children
      .forEach((node: TreeNode) => node.selectable = false);
    }

    var parentSelectable = (event.node.parent.type !== "carrier");
    var parentClear = (!event.node.parent.children
      .some((node: TreeNode) => this.selectedTree.includes(node)));

    if (parentSelectable && parentClear) {
      event.node.parent.selectable = true;
    }
    this.bundleUpdate();
  }

  bundleUpdate() {
    let trips: Trip[];

    if (this.selectedTree === undefined) return;
    this.selectedTree = this.clearDublicates(this.selectedTree);
    trips = this.selectedTree.filter((node: TreeNode) => {
      return (node.type == "trip")
    })
    .map((node: TreeNode) => {
      let trip: Trip = node.data;
      return trip;
    });

    trips.forEach((trip: Trip) => {
      this.ticketClassUpdate(trip);
    });

    this.selectedTrips.emit(trips);
  }

  clearDublicates(nodes: TreeNode[]) : TreeNode[] {
    let uniqueNodes: TreeNode[] = [];
    
    nodes.forEach((node: TreeNode) => {
      if (!uniqueNodes.includes(node)) {
        uniqueNodes.push(node);
      }
    });

    return uniqueNodes;
  }

  ticketClassUpdate(trip: Trip) {
    trip.ticket_classes = [];

    this.selectedTree.filter((node: TreeNode) => {
      return (node.type == "ticketClass")
    })
    .forEach((node: TreeNode) => {
      if (node.parent.data.trip_id == trip.trip_id) {
        trip.ticket_classes.push(node.data);
      }
    });

    trip.ticket_classes.forEach((ticketClass: TicketClass) => {
      this.serviceUpdate(ticketClass);
    });
  }

  serviceUpdate(ticketClass: TicketClass) {
    ticketClass.services = [];

    this.selectedTree.filter((node: TreeNode) => {
      return (node.type == "service")
    })
    .forEach((node: TreeNode) => {
      if (node.parent.data.class_id == ticketClass.class_id) {
        ticketClass.services.push(node.data);
      }
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
