import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ApproverComponent } from './approver.component';
import { ServiceComponent } from './service/service.component';
import { TripComponent } from './trip/trip.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { NotificationsComponent } from './notifications/notifications.component';
import { OpenComponent } from './service/open/open.component';
import { AssignedComponent } from './service/assigned/assigned.component';
import { AssignedTripComponent } from './trip/assigned/assigned.component';
import { OpenTripComponent } from './trip/open/open.component';

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
              component: TripComponent,
              children: [
                {
                    path: '',
                    redirectTo: 'open',
                    pathMatch: 'full',
                },
                {
                    path: 'open',
                    component: OpenTripComponent
                },
                {
                    path: 'assigned',
                    component: AssignedTripComponent
                },                 
              ]
          },
          {
              path: 'service',
              component: ServiceComponent,
              children: [
                {
                    path: '',
                    redirectTo: 'open',
                    pathMatch: 'full',
                },
                {
                    path: 'open',
                    component: OpenComponent
                },
                {
                    path: 'assigned',
                    component: AssignedComponent
                },
              ]
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
