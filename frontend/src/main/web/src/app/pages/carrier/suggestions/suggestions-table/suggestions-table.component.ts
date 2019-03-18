import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { PossibleService } from '../shared/model/possible-service';
import { Suggestion } from '../shared/model/suggestion';
import { TicketClass } from '../shared/model/ticket-class';
import { MessageService } from 'primeng/api';
import { SuggestionService } from '../shared/service/suggestion.service';
import { HttpErrorResponse } from '@angular/common/http';
import { SuggestionDTO } from '../shared/model/suggestionDTO';
import { PossibleServiceService } from 'src/app/services/possible-service.service';
import { Observable } from 'rxjs';
import { forkJoin } from 'rxjs'; 

@Component({
  selector: 'app-suggestions-table',
  templateUrl: './suggestions-table.component.html',
  styleUrls: ['./suggestions-table.component.scss']
})
export class SuggestionsTableComponent implements OnInit {
  @Output() selectedSuggestion = new EventEmitter<Suggestion>();

  @Output() deletedSuggestion = new EventEmitter<Suggestion>();

  @Input() ticketClasses: TicketClass[];

  suggestions: Suggestion[];

  suggestionForEdit: Suggestion;

  constructor(
    private possibleServiceApi: PossibleServiceService,
    private suggestionSrvc: SuggestionService,
    private messageService: MessageService,) {
  }

  ngOnInit() {
    this.getAllSuggestions();
  }
  
  loadTicketClassSuggestions(ticketClass: TicketClass, data: SuggestionDTO[]) {
    let serviceRequest = this.possibleServiceApi.getAll(ticketClass.class_id);
    serviceRequest
    .subscribe(services => {
      ticketClass.services = services;
      data.forEach((suggestionDTO : SuggestionDTO) => {
        console.log(suggestionDTO);
        this.suggestions.push(this.DTOToSuggestion(suggestionDTO));
      })
    }, (error: HttpErrorResponse) => {
      this.showMessage(this.createMessage('error', `Error message - ${error.error.status}`, error.error.message));
    });
  }

  getAllSuggestions() {
    this.suggestions = [];
    this.ticketClasses.forEach(ticketClass => {
      this.getTicketClassSuggestions(ticketClass);
    });
  }

  getTicketClassSuggestions(ticketClass: TicketClass) {
    this.suggestionSrvc.getAll(ticketClass.class_id)
    .subscribe(data => {
      this.loadTicketClassSuggestions(ticketClass, data);
    }, (error: HttpErrorResponse) => {
      if (error.error.status != 404) {
        this.showMessage(this.createMessage('error', `Error message - ${error.error.status}`, error.error.message));
      }
    })
  }

  DTOToSuggestion(suggestionDTO: SuggestionDTO) {
    let ticketClass = this.ticketClasses.find(ticketClass => ticketClass.class_id == suggestionDTO.class_id);
    return new Suggestion(
      suggestionDTO.id,
      ticketClass.services.filter(service => suggestionDTO.pserviceIds.includes(service.id)),
      ticketClass
    )
  }

  onUpdate(suggestion) {
    this.selectedSuggestion.emit(suggestion);
  }

  onDelete(suggestion) {
    this.deletedSuggestion.emit(suggestion);
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
