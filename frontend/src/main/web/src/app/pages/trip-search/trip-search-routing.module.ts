import {RouterModule, Routes} from "@angular/router";
import {TripSearchComponent} from "./trip-search/trip-search.component";
import {NgModule} from "@angular/core";

const routes: Routes = [
  {
    path: '',
    component: TripSearchComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})

export class TripSearchRoutingModule {

}
