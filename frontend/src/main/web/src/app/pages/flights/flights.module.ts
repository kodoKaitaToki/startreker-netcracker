import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FlightsComponent} from './flights.component';
import {FlightsRoutingModule} from "./flights-routing.module";
import {AppHeaderComponent} from './header/app-header.component';
import {LoadingBarHttpClientModule} from "@ngx-loading-bar/http-client";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {AngularFontAwesomeModule} from "angular-font-awesome";
import {TripInfoComponent} from './trip-info/trip-info.component';
import {TicketClassComponent} from './trip-info/ticket-class/ticket-class.component';
import {FooterComponent} from './footer/footer.component';
import {SearchService} from "./shared/services/search.service";
import {SearchBarModule} from '../search-bar/search-bar.module';
import {TicketClassInfoComponent} from './trip-info/ticket-class/ticket-class-info/ticket-class-info.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';

@NgModule({
  declarations: [
    FlightsComponent,
    AppHeaderComponent,
    TripInfoComponent,
    TicketClassComponent,
    FooterComponent,
    TicketClassInfoComponent
  ],
  imports: [
    CommonModule,
    FlightsRoutingModule,
    AngularFontAwesomeModule,
    ReactiveFormsModule,
    LoadingBarHttpClientModule,
    SearchBarModule,
    FormsModule,
    NgbModule
  ],
  providers: [
    SearchService
  ]
})
export class FlightsModule {
}
