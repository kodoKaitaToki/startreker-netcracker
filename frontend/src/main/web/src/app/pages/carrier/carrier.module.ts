import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CarrierRoutingModule } from './carrier-routing.module';
import { NavbarComponent } from './navbar/navbar.component';
import { IndexComponent } from './index/index.component';
import { TripsComponent } from './trips/trips.component';
import { ServicesComponent } from './services/services.component';
import { SuggestionsComponent } from './suggestions/suggestions.component';
import { DiscountsComponent } from './discounts/discounts.component';
import { DashboardsComponent } from './dashboard/dashboards/dashboards.component';
import { CarrierComponent } from './carrier.component';

@NgModule({
  declarations: [NavbarComponent, IndexComponent, TripsComponent, ServicesComponent, SuggestionsComponent, DiscountsComponent, DashboardsComponent, CarrierComponent],
  imports: [
    CommonModule,
    CarrierRoutingModule
  ]
})
export class CarrierModule { }
