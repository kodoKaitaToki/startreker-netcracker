import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ApproverComponent } from './approver.component';
import { ServiceComponent } from './service/service.component';
import { TripComponent } from './trip/trip.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { NotificationsComponent } from './notifications/notifications.component';

const routes: Routes = [
    {
        path: '',
        component: ApproverComponent,
        children: [
          {
              path: '',
              redirectTo: 'dashboard',
              pathMatch: 'full',
          },
          {
              path: 'dashboard',
              component: DashboardComponent
          },
          {
              path: 'trip',
              component: TripComponent
          },
          {
              path: 'service',
              component: ServiceComponent
          },
          {
              path: 'notifications',
              component: NotificationsComponent
          },
        ]
    },

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ApproverRoutingModule { }
