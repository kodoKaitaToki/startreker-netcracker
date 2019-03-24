import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SearchBarComponent } from './search-bar/search-bar.component';
import {ReactiveFormsModule, FormsModule} from "@angular/forms";
import { CalendarModule } from 'primeng/primeng';

@NgModule({
  declarations: [SearchBarComponent],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    CalendarModule
  ],
  exports: [SearchBarComponent]
})
export class SearchBarModule { }
