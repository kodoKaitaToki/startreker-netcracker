import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {FlightsComponent} from "./flights.component";

const routes: Routes = [
  {
    path: '',
    component: FlightsComponent
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class FlightsRoutingModule { }
