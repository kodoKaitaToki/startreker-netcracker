import {Component, OnInit} from '@angular/core';
import {ServiceTripPendingService} from "../shared/service/service-trip-pending.service";
import {PendingService} from "../shared/model/pending-service.model";
import {HttpResponse} from '@angular/common/http';
import {checkToken} from 'src/app/modules/api';
import {clone} from 'ramda';

@Component({
  selector: 'app-pending-services',
  templateUrl: './pending-services.component.html',
  styleUrls: ['./pending-services.component.scss'],
  providers: [ServiceTripPendingService]
})
export class PendingServicesComponent implements OnInit {

  private pendingServcies: PendingService[];

  private filterCriterias = [
    'carrierName',
    'approverName',
    'serviceName',
    'serviceDescr',
    'serviceStatus'
  ];

  totalRec: number;

  page: number = 1;

  entriesAmountOnPage = 3;

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
        this.pendingServcies = clone(resp.body);
      })
  }

  getFilterCriteria($event) {
    this.currentFilterCriteria = $event.value;
  }

  onChangePage($event) {
    this.page = $event;
    window.scrollTo(0, 0);
  }
}
