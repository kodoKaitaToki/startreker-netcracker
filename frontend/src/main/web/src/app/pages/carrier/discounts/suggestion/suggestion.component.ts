import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Suggestion} from "../shared/model/suggestion.model";

@Component({
  selector: 'suggestion',
  templateUrl: './suggestion.component.html',
  styleUrls: ['./suggestion.component.scss']
})
export class SuggestionComponent implements OnInit {

  @Input() suggestion: Suggestion;

  @Output() onAddSuggestionEmitter = new EventEmitter<Suggestion>();

  @Output() onDeleteSuggestionEmitter = new EventEmitter<Suggestion>();

  isDiscountFormActivated = false;

  constructor() {
  }

  ngOnInit() {
  }

  openSuggestionDiscountForm() {
    this.isDiscountFormActivated = true;
  }

  closeDiscountForm() {
    this.isDiscountFormActivated = false;
  }

  emitToMainComponentTicketClassOnDeleteEvent() {
    this.onDeleteSuggestionEmitter.emit(this.suggestion);
  }

  emitToMainComponentSuggestionOnAddEvent($event) {
    this.onAddSuggestionEmitter.emit($event);
  }
}
