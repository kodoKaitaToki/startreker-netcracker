import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CarrierCrudComponent } from './carrier/carrier-crud/carrier-crud.component';
import { BundlesCrudComponent } from './bundles-crud/bundles-crud.component';

import { TripsServicesComponent } from './dashboards/trips-services/trips-services.component';
import { DashboardsComponent } from './dashboards/dashboards.component';
import { ApproverCrudComponent } from './approver/approver-crud/approver-crud.component';
import { CostsComponent } from './dashboards/costs/costs.component';
import { DashboardDeltaComponent } from './dashboards/dashboard-delta/dashboard-delta.component';
import { TroubleStatisticsComponent } from './dashboards/trouble-statistics/trouble-statistics.component';

const routes: Routes = [
  {
      path: '',
      redirectTo: 'dashboards/popularity'
  },
  {
      path: 'approvers',
      component: ApproverCrudComponent
  },
  {
      path: 'carriers',
      component: CarrierCrudComponent
  },
  {
      path: 'bundles',
      component: BundlesCrudComponent
  },
  {
      path: 'dashboards',
      component: DashboardsComponent,
      children: [
              { path: 'costs', component: CostsComponent },
              { path: 'popularity', component: DashboardDeltaComponent},
              { path: 'trouble-tickets', component: TroubleStatisticsComponent},
              { path: 'trips-services-distribution', component: TripsServicesComponent}
          ]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
