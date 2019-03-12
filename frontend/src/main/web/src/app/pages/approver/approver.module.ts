import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {GrowlModule} from "primeng/primeng";
import {MessageService} from 'primeng/components/common/messageservice';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";

import { ApproverRoutingModule } from './approver-routing.module';
import { ApproverComponent } from './approver.component';
import { NavbarComponent } from './navbar/navbar.component';
import { ServiceComponent } from './service/service.component';
import { TripComponent } from './trip/trip.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { NotificationsComponent } from './notifications/notifications.component';
import { OpenComponent } from './service/open/open.component';
import { AssignedComponent } from './service/assigned/assigned.component';
import { AssignedTripComponent } from './trip/assigned/assigned.component';
import { OpenTripComponent } from './trip/open/open.component';

@NgModule({
  declarations: [ServiceComponent, 
                ApproverComponent, 
                NavbarComponent, 
                TripComponent, 
                DashboardComponent, 
                NotificationsComponent, 
                OpenComponent, 
                AssignedComponent,
                AssignedTripComponent,
                OpenTripComponent],
  imports: [
    CommonModule,
    ApproverRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    GrowlModule
  ],
  providers:[
    MessageService
  ]
})
export class ApproverModule { }
