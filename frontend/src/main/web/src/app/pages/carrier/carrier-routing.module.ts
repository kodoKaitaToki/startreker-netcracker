import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CarrierComponent } from './carrier.component';
import { IndexComponent } from './index/index.component';
import { TripsComponent } from './trips/trips.component';
import { ServicesComponent } from './services/services.component';
import { SuggestionsComponent } from './suggestions/suggestions.component';
import { DiscountsComponent } from './discounts/discounts.component';
import { DashboardsComponent } from './dashboard/dashboards/dashboards.component';
import { SalesComponent } from './dashboard/sales/sales.component';
import { ViewsComponent } from './dashboard/views/views.component';

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
              component: DashboardsComponent,
              children: [
                {
                    path: 'sales',
                    component: SalesComponent
                },
                {
                    path: 'views',
                    component: ViewsComponent
                }
              ]
          },
        ]
    },

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CarrierRoutingModule { }
