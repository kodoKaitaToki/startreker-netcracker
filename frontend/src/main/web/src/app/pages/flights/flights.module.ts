import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FlightsComponent} from './flights.component';
import {FlightsRoutingModule} from "./flights-routing.module";
import {AppHeaderComponent} from './header/app-header.component';
import { SearchPanelComponent } from './search-panel/search-panel.component';
import {LoadingBarHttpClientModule} from "@ngx-loading-bar/http-client";
import {ReactiveFormsModule} from "@angular/forms";
import {AngularFontAwesomeModule} from "angular-font-awesome";
import { TripInfoComponent } from './trip-info/trip-info.component';
import { TicketClassComponent } from './trip-info/ticket-class/ticket-class.component';
import { FooterComponent } from './footer/footer.component';

@NgModule({
  declarations: [
    FlightsComponent,
    AppHeaderComponent,
    SearchPanelComponent,
    TripInfoComponent,
    TicketClassComponent,
    FooterComponent
  ],
  imports: [
    CommonModule,
    FlightsRoutingModule,
    AngularFontAwesomeModule,
    ReactiveFormsModule,
    LoadingBarHttpClientModule
  ]
})
export class FlightsModule {
}
