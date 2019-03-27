import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ApproverComponent} from './approver.component';
import {ServiceComponent} from './service/service.component';
import {TripComponent} from './trip/trip.component';
import {DashboardComponent} from './dashboard/dashboard-main-page/dashboard.component';
import {NotificationsComponent} from './notifications/notifications.component';
import {OpenComponent} from './service/open/open.component';
import {AssignedComponent} from './service/assigned/assigned.component';
import {AssignedTripComponent} from './trip/assigned/assigned.component';
import {OpenTripComponent} from './trip/open/open.component';
import {PendingServicesComponent} from "./dashboard/pending-services/pending-services.component";
import {PendingTripsComponent} from "./dashboard/pending-trips/pending-trips.component";

const routes: Routes = [
  {
    path: '',
    component: ApproverComponent,
    children: [
      {
        path: '',
        redirectTo: 'dashboard',
      },
      {
        path: 'dashboard',
        component: DashboardComponent,
        children: [
          {
            path: 'pending-services',
            component: PendingServicesComponent
          },
          {
            path: 'pending-trips',
            component: PendingTripsComponent
          }
        ]
      },
      {
        path: 'trip',
        component: TripComponent,
        children: [
          {
            path: '',
            redirectTo: 'open'
          },
          {
            path: 'open',
            component: OpenTripComponent
          },
          {
            path:'assigned',
            component: AssignedTripComponent
          }
        ]
      },
      {
        path: 'service',
        component: ServiceComponent,
        children: [
          {
            path: '',
            redirectTo: 'opened'
          },
          {
            path: 'opened',
            component: OpenComponent
          },
          {
            path: 'assigned',
            component: AssignedComponent
          },
        ]
      },
    ]
  },

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ApproverRoutingModule {
}
