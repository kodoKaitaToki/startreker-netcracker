import { Component, OnInit, Input } from '@angular/core';
import { Trip } from '../trips/shared/model/trip';

@Component({
  selector: 'app-suggestions',
  templateUrl: './suggestions.component.html',
  styleUrls: ['./suggestions.component.scss']
})
export class SuggestionsComponent implements OnInit {
  @Input() trip: Trip;

  isAdding: boolean = false;

  constructor() { }

  ngOnInit() {
  }

  addSuggestion() {
    this.isAdding = true;
  }

  cancelSuggestion() {
    this.isAdding = false;
  }

}
