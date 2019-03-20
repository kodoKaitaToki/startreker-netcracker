import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {GrowlModule} from "primeng/primeng";
import {MessageService} from 'primeng/components/common/messageservice';

import {ApproverRoutingModule} from './approver-routing.module';
import {ApproverComponent} from './approver.component';
import {NavbarComponent} from './navbar/navbar.component';
import {ServiceComponent} from './service/service.component';
import {TripComponent} from './trip/trip.component';
import {DashboardComponent} from './dashboard/dashboard-main-page/dashboard.component';
import {NotificationsComponent} from './notifications/notifications.component';
import {OpenComponent} from './service/open/open.component';
import {AssignedComponent} from './service/assigned/assigned.component';
import {AssignedTripComponent} from './trip/assigned/assigned.component';
import {OpenTripComponent} from './trip/open/open.component';
import {SharedModule} from 'src/app/shared/shared.module';
import {PendingTripsComponent} from './dashboard/pending-trips/pending-trips.component';
import {PendingServicesComponent} from './dashboard/pending-services/pending-services.component';
import {ServiceTripPendingService} from "./dashboard/shared/service/service-trip-pending.service";
import {SelectInfoPipe} from './dashboard/shared/pipes/select-info.pipe';
import {ItemsFilter} from './dashboard/shared/pipes/trip-filter.pipe';
import {NgxPaginationModule} from "ngx-pagination";
import {SelectServiceInfoPipe} from './dashboard/shared/pipes/select-service-info.pipe';
import {StatePipe} from "./dashboard/shared/pipes/StatePipe";

@NgModule({
  declarations: [ServiceComponent,
    ApproverComponent,
    NavbarComponent,
    TripComponent,
    DashboardComponent,
    NotificationsComponent,
    OpenComponent,
    AssignedComponent,
    AssignedTripComponent,
    OpenTripComponent,
    PendingTripsComponent,
    PendingServicesComponent,
    SelectInfoPipe, ItemsFilter, SelectServiceInfoPipe, StatePipe
  ],
  imports: [
    CommonModule,
    ApproverRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    SharedModule,
    GrowlModule,
    NgxPaginationModule
  ],
  providers: [
    MessageService, ServiceTripPendingService
  ]
})
export class ApproverModule {
}
