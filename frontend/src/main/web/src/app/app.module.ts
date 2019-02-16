import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HttpClientModule} from "@angular/common/http";
import {TestComponent} from './test/test.component';
import {CarouselComponent} from './carousel/carousel.component';
import { MapComponent } from './map/map.component';
import { SearchMenuComponent } from './search-menu/search-menu.component';
import { FeaturesComponent } from './features/features.component';
import { FooterComponent } from './footer/footer.component';

@NgModule({
  declarations: [
    AppComponent,
    TestComponent,
    CarouselComponent,
    MapComponent,
    SearchMenuComponent,
    FeaturesComponent,
    FooterComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
