import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {UserComponent} from "./user.component";
import {NavbarComponent} from "./navbar/navbar.component";
import {UserRoutingModule} from "./user-routing.module";
import {HistoryComponent} from './history/history.component';
import {ReactiveFormsModule} from "@angular/forms";
import {NgxPaginationModule} from "ngx-pagination";
import {ShowMessageService} from "../admin/approver/shared/service/show-message.service";
import {MessageService} from "primeng/api";
import {ToastModule} from "primeng/toast";

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
    NgxPaginationModule,
    ToastModule,
  ],
  providers: [
    ShowMessageService,
    MessageService
  ]
})
export class UserModule {
}
