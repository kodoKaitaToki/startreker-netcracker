import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {UserComponent} from "./user.component";
import {HistoryComponent} from "./history/history.component";
import { PurchasePageComponent } from './purchase-page/purchase-page.component';

const routes: Routes = [
  {
    path: '',
    component: UserComponent,
    children: [
      {
        path: '',
        redirectTo: 'history'
      },
      {
        path: 'history',
        component: HistoryComponent
      },
      {
        path: 'cart',
        component: PurchasePageComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UserRoutingModule {
}
