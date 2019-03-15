import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

import { ApproverRoutingModule } from './approver-routing.module';
import { ApproverComponent } from './approver.component';
import { NavbarComponent } from './navbar/navbar.component';
import { ServiceComponent } from './service/service.component';
import { TripComponent } from './trip/trip.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { NotificationsComponent } from './notifications/notifications.component';
import { OpenComponent } from './service/open/open.component';
import { AssignedComponent } from './service/assigned/assigned.component';
import { SharedModule } from 'src/app/shared/shared.module';

@NgModule({
  declarations: [ServiceComponent, ApproverComponent, NavbarComponent, TripComponent, DashboardComponent, NotificationsComponent, OpenComponent, AssignedComponent],
  imports: [
    CommonModule,
    ApproverRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    SharedModule,
  ]
})
export class ApproverModule { }
