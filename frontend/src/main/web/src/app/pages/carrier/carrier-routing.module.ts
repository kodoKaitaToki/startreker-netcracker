import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CarrierComponent } from './carrier.component';
import { IndexComponent } from './index/index.component';
import { TripsComponent } from './trips/trips.component';
import { ServicesComponent } from './services/services.component';
import { SuggestionsComponent } from './suggestions/suggestions.component';
import { DiscountsComponent } from './discounts/discounts.component';
import { DashboardsComponent } from './dashboard/dashboards/dashboards.component';

const routes: Routes = [
    {
        path: '',
        component: CarrierComponent,
        children: [
          {
              path: '',
              component: IndexComponent
          },
          {
              path: 'trips',
              component: TripsComponent
          },
          {
              path: 'services',
              component: ServicesComponent
          },
          {
              path: 'suggestions',
              component: SuggestionsComponent
          },
          {
              path: 'discounts',
              component: DiscountsComponent
          },
          {
              path: 'dashboards',
              component: DashboardsComponent
          },
        ]
    },

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CarrierRoutingModule { }
