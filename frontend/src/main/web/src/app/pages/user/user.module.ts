import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {UserComponent} from "./user.component";
import {NavbarComponent} from "./navbar/navbar.component";
import {UserRoutingModule} from "./user-routing.module";
import {HistoryComponent} from './history/history.component';
import {ReactiveFormsModule} from "@angular/forms";

@NgModule({
  declarations: [
    UserComponent,
    NavbarComponent,
    HistoryComponent
  ],
  imports: [
    CommonModule,
    UserRoutingModule,
    ReactiveFormsModule,
  ]
})
export class UserModule {
}
