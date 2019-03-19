import { Component, OnInit, Input, ViewChild } from '@angular/core';
import { Trip } from '../trips/shared/model/trip';
import { Suggestion } from './shared/model/suggestion';
import { SuggestionService } from './shared/service/suggestion.service';
import { MessageService } from 'primeng/api';
import { SuggestionDTO } from './shared/model/suggestionDTO';
import { SuggestionsTableComponent } from './suggestions-table/suggestions-table.component';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { SuggestionsTreeComponent } from './suggestions-tree/suggestions-tree.component';
import { checkToken } from 'src/app/modules/api';

@Component({
  selector: 'app-suggestions',
  templateUrl: './suggestions.component.html',
  styleUrls: ['./suggestions.component.scss']
})
export class SuggestionsComponent implements OnInit {
  @ViewChild(SuggestionsTableComponent) table:SuggestionsTableComponent;
  @ViewChild(SuggestionsTreeComponent) tree:SuggestionsTreeComponent;

  @Input() trip: Trip;

  isAdding: boolean = false;

  constructor(
    private suggestionSrvc: SuggestionService,
    private messageService: MessageService,) {
  }

  ngOnInit() {
  }

  addSuggestion() {
    this.isAdding = true;
  }

  postSuggestion(suggestion: Suggestion) {
    this.isAdding = false;
    if (suggestion.id === undefined || suggestion.id === null) {
      this.createSuggestion(suggestion);
    } else {
      this.updateSuggestion(suggestion);
    }
  }

  createSuggestion(suggestion: Suggestion) {
    this.suggestionSrvc.postSuggestion(this.SuggestionToDTO(suggestion))
    .subscribe((resp: HttpResponse<any>) => {
      checkToken(resp.headers);
      this.showMessage(this.createMessage('success', 'Suggestion post', 'The suggestion was created'));
      this.table.getAllSuggestions();
    }, (error: HttpErrorResponse) => {
      this.showMessage(this.createMessage('error', `Error message - ${error.error.status}`, error.error.error));
    });
  }

  updateSuggestion(suggestion: Suggestion) {
    this.suggestionSrvc.putSuggestion(this.SuggestionToDTO(suggestion))
    .subscribe((resp: HttpResponse<any>) => {
      checkToken(resp.headers);
      this.showMessage(this.createMessage('success', 'Suggestion put', 'The suggestion was updated'));
      this.table.getAllSuggestions();
    }, (error: HttpErrorResponse) => {
      this.showMessage(this.createMessage('error', `Error message - ${error.error.status}`, error.error.error));
    });
  }

  deleteSuggestion(suggestion: Suggestion) {
    this.table.suggestions = this.table.suggestions.filter(s => {s.id !== suggestion.id});
    this.suggestionSrvc.deleteSuggestion(this.SuggestionToDTO(suggestion))
    .subscribe((resp: HttpResponse<any>) => {
      checkToken(resp.headers);
      this.showMessage(this.createMessage('success', 'Suggestion delete', 'The suggestion was deleted'));
      this.table.getAllSuggestions();
    }, (error: HttpErrorResponse) => {
      this.showMessage(this.createMessage('error', `Error message - ${error.error.status}`, error.error.error));
    });
  }

  editSuggestion(suggestion: Suggestion) {
    this.isAdding = true;
    this.tree.recieveSuggestion(suggestion);
  }

  cancelSuggestion() {
    this.isAdding = false;
  }

  SuggestionToDTO(suggestion: Suggestion) {
    return new SuggestionDTO(
      suggestion.id,
      suggestion.services.map(service => service.id),
      suggestion.ticketClass.class_id
    )
  }

  createMessage(severity: string, summary: string, detail: string): any {
    return {
      severity: severity,
      summary: summary,
      detail: detail
    };
  }

  showMessage(msgObj: any) {
    this.messageService.add(msgObj);
  }

}
