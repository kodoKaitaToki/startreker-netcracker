import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {UserComponent} from "./user.component";
import {NavbarComponent} from "./navbar/navbar.component";
import {UserRoutingModule} from "./user-routing.module";
import {HistoryComponent} from './history/history.component';
import {ReactiveFormsModule} from "@angular/forms";
import {NgxPaginationModule} from "ngx-pagination";
import {CheckboxModule} from 'primeng/checkbox';
import { PurchasePageComponent } from './purchase-page/purchase-page.component';

@NgModule({
  declarations: [
    UserComponent,
    NavbarComponent,
    HistoryComponent,
    PurchasePageComponent
  ],
  imports: [
    CommonModule,
    UserRoutingModule,
    ReactiveFormsModule,
    NgxPaginationModule,
    CheckboxModule
  ]
})
export class UserModule {
}
