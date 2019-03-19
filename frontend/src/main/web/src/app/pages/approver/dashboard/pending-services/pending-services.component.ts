import {Component, OnInit} from '@angular/core';
import {ServiceTripPendingService} from "../shared/service/service-trip-pending.service";
import {PendingService} from "../shared/model/pending-service.model";
import { HttpResponse } from '@angular/common/http';
import { checkToken } from 'src/app/modules/api';
import { clone } from 'ramda';

@Component({
  selector: 'app-pending-services',
  templateUrl: './pending-services.component.html',
  styleUrls: ['./pending-services.component.scss'],
  providers: [ServiceTripPendingService]
})
export class PendingServicesComponent implements OnInit {

  private pendingServcies: PendingService[];

  private currentPendingService: PendingService = null;

  private filterCriterias = [
    'carrierName',
    'approverName',
    'serviceName',
    'serviceDescr',
    'serviceStatus'
  ];

  private currentFilterCriteria: string;

  private currentFilterContent: string;

  constructor(private pendingSrvc: ServiceTripPendingService) {
  }

  ngOnInit() {
    this.getAllPendingServices();
  }

  public getAllPendingServices() {
    this.pendingSrvc.getAllPendingServices()
      .subscribe((resp: HttpResponse<any>) => {
        checkToken(resp.headers);
        this.pendingServcies = clone(resp);
      })
  }

  onDetailClick(pendingService) {
    this.currentPendingService = pendingService;
  }

  onCloseDetailClick() {
    this.currentPendingService = null;
  }

  getFilterCriteria($event) {
    this.currentFilterCriteria = $event.value;
  }
}
