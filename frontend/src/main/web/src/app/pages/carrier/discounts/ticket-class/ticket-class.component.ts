import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {TicketClass} from "../shared/model/ticket-class.model";

@Component({
  selector: 'ticket-class',
  templateUrl: './ticket-class.component.html',
  styleUrls: ['./ticket-class.component.scss']
})
export class TicketClassComponent implements OnInit {

  @Input() ticketClass: TicketClass;

  @Output() onAddTicketClassEmitter = new EventEmitter<TicketClass>();

  @Output() onDeleteTicketClassEmitter = new EventEmitter<TicketClass>();

  isDiscountFormActivated = false;

  constructor() {
  }

  ngOnInit() {
  }

  openTicketClassDiscountForm() {
    this.isDiscountFormActivated = true;
  }

  closeDiscountForm() {
    this.isDiscountFormActivated = false;
  }

  emitToMainComponentTicketClassOnDeleteEvent() {
    this.onDeleteTicketClassEmitter.emit(this.ticketClass);
  }

  emitToMainComponentTicketClassOnAddEvent($event) {
    this.onAddTicketClassEmitter.emit($event);
  }
}
