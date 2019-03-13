import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {AdminRoutingModule} from './admin-routing.module';
import {NavbarComponent} from './navbar/navbar.component';
import {ApproverCrudComponent} from './approver/approver-crud/approver-crud.component';
import {CarrierCrudComponent} from './carrier/carrier-crud/carrier-crud.component';
import {DashboardsComponent} from './dashboards/dashboards.component';
import {BundlesCrudComponent} from './bundles-crud/bundles-crud.component';
import {ApproverComponentComponent} from './approver/approver-component/approver-component.component';
import {ApproverTableComponent} from './approver/approver-table/approver-table.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {FilterPipePipe} from './carrier/filter-pipe.pipe';
import {AprroverShowStatusPipe} from './approver/shared/pipes/aprrover-show-status.pipe';

import {TripsServicesComponent} from './dashboards/trips-services/trips-services.component';
import {TripDashboardComponent} from './dashboards/trips-services/trip-dashboard/trip-dashboard.component';
import {ServiceDashboardComponent} from './dashboards/trips-services/service-dashboard/service-dashboard.component';

import {DashboardDeltaComponent} from './dashboards/dashboard-delta/dashboard-delta.component';
import {TroubleStatisticsComponent} from './dashboards/trouble-statistics/trouble-statistics.component';

import {CarrierShowStatusPipe} from './carrier/carrier-show-status.pipe';

import {CostsComponent} from './dashboards/costs/costs.component';
import {CommonChartComponent} from './dashboards/costs/common-chart/common-chart.component';
import {CarCostDashComponent} from './dashboards/costs/car-cost-dash/car-cost-dash.component';

import {CarrierComponentComponent} from './carrier/carrier-component/carrier-component.component';
import {CarrierTableComponent} from './carrier/carrier-table/carrier-table.component';
import {NgxPaginationModule} from 'ngx-pagination';

import {TroubleStatisticsService} from './dashboards/trouble-statistics.service';
import {ApproverService} from "./approver/shared/service/approver.service";
import {ApproverFilterPipe} from './approver/shared/pipes/approver-filter.pipe';
import {GrowlModule, MessageService, ProgressBarModule} from "primeng/primeng";
import {ToastModule} from "primeng/toast";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";


@NgModule({
  declarations: [
    NavbarComponent,
    ApproverCrudComponent,
    CarrierCrudComponent,
    BundlesCrudComponent,
    DashboardsComponent,
    ApproverComponentComponent,
    ApproverTableComponent,
    ApproverCrudComponent,
    FilterPipePipe,
    AprroverShowStatusPipe,
    TripsServicesComponent,
    TripDashboardComponent,
    ServiceDashboardComponent,
    DashboardDeltaComponent,
    TroubleStatisticsComponent,
    CarrierComponentComponent,
    CarrierTableComponent,
    CarrierShowStatusPipe,
    CommonChartComponent,
    CarCostDashComponent,
    CostsComponent,
    ApproverFilterPipe
  ],
  imports: [
    CommonModule,
    AdminRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    NgxPaginationModule,
    ToastModule,
    ProgressBarModule
  ],
  providers: [
    AprroverShowStatusPipe,
    TroubleStatisticsService,
    ApproverService,
    ApproverFilterPipe,
    MessageService
  ]
})
export class AdminModule {
}
