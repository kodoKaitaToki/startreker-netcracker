import {CommonModule} from '@angular/common';
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {NgModule} from '@angular/core';
import {HttpClientModule} from "@angular/common/http";
import {ReactiveFormsModule, FormsModule} from '@angular/forms';
import { LoadingBarHttpClientModule } from '@ngx-loading-bar/http-client';
import {MessageService, GrowlModule, CalendarModule} from "primeng/primeng";

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {LandingComponent} from './pages/landing/landing.component';
import {CarouselComponent} from './pages/landing/carousel/carousel.component';
import {MapComponent} from './pages/landing/map/map.component';
import {FeaturesComponent} from './pages/landing/features/features.component';
import {FooterComponent} from './pages/landing/footer/footer.component';
import {NotFoundPageComponent} from './pages/not-found-page/not-found-page.component';
import {HeaderComponent} from './pages/landing/header/header.component';
import {RegistrationComponent} from './pages/registration/registration.component';
import {LoginComponent} from './pages/login/login.component';
import {AngularFontAwesomeModule} from 'angular-font-awesome';
import {RecoveryComponent} from './pages/recovery/recovery.component';

import {FlightsModule} from "./pages/flights/flights.module";
import { Configuration } from './app.constants';
import { ConfirmMessageComponent } from './pages/confirm-message/confirm-message.component';
import { ComingSoonComponent } from './pages/coming-soon/coming-soon.component';
import { DataService } from './shared/data.service';
import { SearchBarModule } from './pages/search-bar/search-bar.module';

@NgModule({
  declarations: [
    AppComponent,
    CarouselComponent,
    MapComponent,
    FeaturesComponent,
    FooterComponent,
    NotFoundPageComponent,
    LandingComponent,
    HeaderComponent,
    RegistrationComponent,
    LoginComponent,
    RecoveryComponent,
    ConfirmMessageComponent,
    ComingSoonComponent
  ],
  imports: [
    CommonModule,
    AppRoutingModule,
    HttpClientModule,
    AngularFontAwesomeModule,
    ReactiveFormsModule,
    LoadingBarHttpClientModule,
    BrowserModule,
    BrowserAnimationsModule,
    GrowlModule,
    FormsModule,
    CalendarModule,
    FlightsModule,
    SearchBarModule
  ],

  providers: [Configuration, MessageService, DataService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
