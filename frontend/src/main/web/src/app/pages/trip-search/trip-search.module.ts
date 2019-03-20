import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TripSearchComponent } from './trip-search/trip-search.component';
import { TripSearchRoutingModule } from "./trip-search-routing.module";
import { SearchResultComponent } from './search-result/search-result.component';
import { ReactiveFormsModule } from "@angular/forms";

@NgModule({
  declarations: [
    TripSearchComponent,
    SearchResultComponent
  ],
  imports: [
    CommonModule,
    TripSearchRoutingModule,
    ReactiveFormsModule
  ]
})
export class TripSearchModule { }
