import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ApproverRoutingModule } from './approver-routing.module';
import { ApproverComponent } from './approver.component';
import { NavbarComponent } from './navbar/navbar.component';
import { ServiceComponent } from './service/service.component';
import { TripComponent } from './trip/trip.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { NotificationsComponent } from './notifications/notifications.component';

@NgModule({
  declarations: [ServiceComponent, ApproverComponent, NavbarComponent, TripComponent, DashboardComponent, NotificationsComponent],
  imports: [
    CommonModule,
    ApproverRoutingModule
  ]
})
export class ApproverModule { }
