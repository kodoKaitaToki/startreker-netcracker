import {Component, Input, OnInit} from '@angular/core';
import {FlightClass} from "../../shared/models/flight-class.model";
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {SearchService} from "../../shared/services/search.service";
import {TicketClassInfoComponent} from "./ticket-class-info/ticket-class-info.component";
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-ticket-class',
  templateUrl: './ticket-class.component.html',
  styleUrls: ['./ticket-class.component.scss'],
})
export class TicketClassComponent implements OnInit {

  @Input('ticket_class') ticket_class: FlightClass;
  amountForm: FormGroup;

  private amount: number;

  constructor(private searchSvc: SearchService,
              private modalService: NgbModal) {
    this.amountForm = new FormGroup({
      amount: new FormControl({value: 1}, [Validators.required, Validators.min(1)])
    })
  }

  ngOnInit() {

  }

  openVerticallyCentered() {
    const modalRef = this.modalService.open(TicketClassInfoComponent, {centered: true, size: 'lg'});
    modalRef.componentInstance.ticket_class = this.ticket_class;
  }

  onAmountChanged() {
    this.amount = this.amountForm.get('amount').value;

    if (this.amount < 1) {
      this.amount = 1;
    }
    if (this.amount > this.ticket_class.remaining_seats) {
      this.amount = this.ticket_class.remaining_seats;
    }
    this.amountForm.patchValue({
      amount: this.amount
    });
  }
}
