import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Suggestion} from "../shared/model/suggestion.model";
import {Trip} from "../shared/model/trip.model";

@Component({
  selector: 'suggestion',
  templateUrl: './suggestion.component.html',
  styleUrls: ['./suggestion.component.scss']
})
export class SuggestionComponent implements OnInit {

  @Input() trip: Trip;

  @Output() onAddSuggestionEmitter = new EventEmitter<Suggestion>();

  @Output() onDeleteSuggestionEmitter = new EventEmitter<Suggestion>();

  isDiscountFormActivated = false;

  currentSuggestion: Suggestion;

  constructor() {
  }

  ngOnInit() {
  }

  openSuggestionDiscountForm(suggestion) {
    this.currentSuggestion = suggestion;
    this.isDiscountFormActivated = true;
  }

  closeDiscountForm() {
    this.currentSuggestion = null;
    this.isDiscountFormActivated = false;
  }

  emitToMainComponentSuggestionOnDeleteEvent($event) {
    this.onDeleteSuggestionEmitter.emit($event);
  }

  emitToMainComponentSuggestionOnAddEvent($event) {
    this.onAddSuggestionEmitter.emit($event);
  }
}
