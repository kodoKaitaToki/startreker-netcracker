import {Component, OnInit} from '@angular/core';
import {ServiceTripPendingService} from "../shared/service/service-trip-pending.service";
import {PendingService} from "../shared/model/pending-service.model";

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
      .subscribe((data) => {
        this.pendingServcies = data;
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
