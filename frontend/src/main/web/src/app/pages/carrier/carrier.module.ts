import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { CarrierRoutingModule } from './carrier-routing.module';
import { NavbarComponent } from './navbar/navbar.component';
import { IndexComponent } from './index/index.component';
import { TripsComponent } from './trips/trips.component';
import { ServicesComponent } from './services/services.component';
import { SuggestionsComponent } from './suggestions/suggestions.component';
import { DiscountsComponent } from './discounts/discounts.component';
import { DashboardsComponent } from './dashboard/dashboards/dashboards.component';
import { CarrierComponent } from './carrier.component';
import { SalesComponent } from './dashboard/sales/sales.component';
import { ViewsComponent } from './dashboard/views/views.component';
import { ServiceTableComponent } from './services/service-table/service-table.component';
import { ServiceCrudComponent } from './services/service-crud/service-crud.component';
import { ClarificationComponent } from './services/clarification/clarification.component';
import { ArchiveComponent } from './services/archive/archive.component';
import { TripsTableComponent } from './trips/trips-table/trips-table.component';
import { TripsCrudComponent } from './trips/trips-crud/trips-crud.component';
import { AprroverShowStatusPipe } from './trips/shared/pipes/aprrover-show-status.pipe';
import { FilterPipePipe } from './trips/shared/pipes/filter-pipe.pipe';

@NgModule({
  declarations: [
    NavbarComponent, 
    IndexComponent, 
    TripsComponent, 
    ServicesComponent, 
    SuggestionsComponent, 
    DiscountsComponent, 
    DashboardsComponent, 
    CarrierComponent, 
    SalesComponent, 
    ViewsComponent, 
    ServiceTableComponent, 
    ServiceCrudComponent, 
    ClarificationComponent, 
    ArchiveComponent,
    TripsTableComponent,
    TripsCrudComponent,
    AprroverShowStatusPipe,
    FilterPipePipe
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    CarrierRoutingModule
  ]
})
export class CarrierModule { }
