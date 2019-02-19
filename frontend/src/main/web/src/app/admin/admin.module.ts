import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdminRoutingModule } from './admin-routing.module';
import { NavbarComponent } from './navbar/navbar.component';
import { IndexComponent } from './index/index.component';
import { ApproverCrudComponent } from './approver-crud/approver-crud.component';
import { CarrierCrudComponent } from './carrier/carrier-crud/carrier-crud.component';
import { BundlesCrudComponent } from './bundles-crud/bundles-crud.component';
import { UserDetailsComponent } from './user-details/user-details.component';
import { CarrierComponentComponent } from './carrier/carrier-component/carrier-component.component';
import { CarrierTableComponent } from './carrier/carrier-table/carrier-table.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { FilterPipePipe } from './carrier/filter-pipe.pipe';
import { CarrierShowStatusPipe } from './carrier/carrier-show-status.pipe';
import { CommonChartComponent } from './dashboard/common-chart/common-chart.component';
import { CarCostDashComponent } from './dashboard/car-cost-dash/car-cost-dash.component';
import { DashboardsComponent } from './dashboard/dashboards/dashboards.component';
import { DashboardChartsComponent } from './dashboard-charts/dashboard-charts.component';

@NgModule({
  declarations: [NavbarComponent, ApproverCrudComponent, IndexComponent, CarrierCrudComponent, BundlesCrudComponent, DashboardsComponent, UserDetailsComponent, DashboardChartsComponent,
    CarrierComponentComponent,
    CarrierTableComponent,
    FilterPipePipe,
    CarrierShowStatusPipe,
    CommonChartComponent,
    CarCostDashComponent],
  imports: [
    CommonModule,
    AdminRoutingModule,
    FormsModule,
    ReactiveFormsModule
  ]
})
export class AdminModule { }
