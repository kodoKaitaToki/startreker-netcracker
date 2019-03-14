import {RouterModule, Routes} from "@angular/router";
import {TripSearchComponent} from "./trip-search/trip-search.component";
import {NgModule} from "@angular/core";
import {SearchResultComponent} from "./search-result/search-result.component";

const routes: Routes = [
  {
    path: '',
    component: TripSearchComponent,
    children: [
      {
        path: '',
        component: SearchResultComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})

export class TripSearchRoutingModule {

}
