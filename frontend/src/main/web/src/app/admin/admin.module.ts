import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdminRoutingModule } from './admin-routing.module';
import { NavbarComponent } from './navbar/navbar.component';
import { IndexComponent } from './index/index.component';
import { ApproverCrudComponent } from './approver-crud/approver-crud.component';
import { CarrierCrudComponent } from './carrier-crud/carrier-crud.component';
import { BundlesCrudComponent } from './bundles-crud/bundles-crud.component';
import { DashboardsComponent } from './dashboards/dashboards.component';
import { UserDetailsComponent } from './user-details/user-details.component';
import { DashboardChartsComponent } from './dashboard-charts/dashboard-charts.component';
import { ListsChartsComponent } from './lists-charts/lists-charts.component';

@NgModule({
  declarations: [NavbarComponent, ApproverCrudComponent, IndexComponent, CarrierCrudComponent, BundlesCrudComponent, DashboardsComponent, UserDetailsComponent, DashboardChartsComponent, ListsChartsComponent],
  imports: [
    CommonModule,
    AdminRoutingModule
  ]
})
export class AdminModule { }
