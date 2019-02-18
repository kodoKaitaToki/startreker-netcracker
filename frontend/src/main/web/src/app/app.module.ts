import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {HttpClientModule} from "@angular/common/http";
import {ReactiveFormsModule} from '@angular/forms';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';

import { LandingComponent } from './pages/landing/landing.component';
import {CarouselComponent} from './pages/landing/carousel/carousel.component';
import { MapComponent } from './pages/landing/map/map.component';
import { SearchMenuComponent } from './pages/landing/search-menu/search-menu.component';
import { FeaturesComponent } from './pages/landing/features/features.component';
import { FooterComponent } from './pages/landing/footer/footer.component';
import { NotFoundPageComponent } from './pages/not-found-page/not-found-page.component';
import { HeaderComponent } from './pages/landing/header/header.component';

import { RegistrationComponent } from './pages/registration/registration.component';
import { LoginComponent } from './pages/login/login.component';
import { AngularFontAwesomeModule } from 'angular-font-awesome';

@NgModule({
  declarations: [
    AppComponent,
    CarouselComponent,
    MapComponent,
    SearchMenuComponent,
    FeaturesComponent,
    FooterComponent,
    NotFoundPageComponent,
    LandingComponent,
    HeaderComponent,
    RegistrationComponent,
    LoginComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    AngularFontAwesomeModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
