import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {AdminRoutingModule} from './admin-routing.module';
import {NavbarComponent} from './navbar/navbar.component';
import {IndexComponent} from './index/index.component';
import {ApproverCrudComponent} from './approver/approver-crud/approver-crud.component';
import {CarrierCrudComponent} from './carrier-crud/carrier-crud.component';
import {BundlesCrudComponent} from './bundles-crud/bundles-crud.component';
import {DashboardsComponent} from './dashboards/dashboards.component';
import {UserDetailsComponent} from './user-details/user-details.component';
import {DashboardChartsComponent} from './dashboard-charts/dashboard-charts.component';
import {ListsChartsComponent} from './lists-charts/lists-charts.component';
import {ApproverComponentComponent} from './approver/approver-component/approver-component.component';
import {ApproverTableComponent} from './approver/approver-table/approver-table.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { FilterPipePipe } from './approver/shared/pipes/filter-pipe.pipe';
import { AprroverShowStatusPipe } from './approver/shared/pipes/aprrover-show-status.pipe';
import { DashboardDeltaComponent } from './dashboard-delta/dashboard-delta.component';

@NgModule({
  declarations: [
    NavbarComponent,
    ApproverCrudComponent,
    IndexComponent,
    CarrierCrudComponent,
    BundlesCrudComponent,
    DashboardsComponent,
    UserDetailsComponent,
    DashboardChartsComponent,
    ListsChartsComponent,
    ApproverComponentComponent,
    ApproverTableComponent,
    ApproverCrudComponent,
    FilterPipePipe,
    AprroverShowStatusPipe,
    DashboardDeltaComponent
  ],
  imports: [
    CommonModule,
    AdminRoutingModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [FilterPipePipe]
})
export class AdminModule {
}
