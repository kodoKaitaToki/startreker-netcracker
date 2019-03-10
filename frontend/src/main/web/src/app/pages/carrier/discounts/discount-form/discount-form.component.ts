import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Suggestion} from "../shared/model/suggestion.model";
import {TicketClass} from "../shared/model/ticket-class.model";

@Component({
  selector: 'discount-form',
  templateUrl: './discount-form.component.html',
  styleUrls: ['./discount-form.component.scss']
})
export class DiscountFormComponent implements OnInit {

  @Input() obj: (Suggestion | TicketClass);

  @Input() isDiscountFormActivated;

  @Output() closeDiscountFormNotifier = new EventEmitter();

  @Output() submitFormEmitter = new EventEmitter<(Suggestion | TicketClass)>();

  isInputtedDateIncorrect = false;

  valueWithDiscount: number;

  form = new FormGroup({
    start_date: new FormControl('', Validators.required),
    finish_date: new FormControl('', Validators.required),
    is_percent: new FormControl(false, Validators.required),
    discount_rate: new FormControl('', [Validators.required, Validators.pattern('[\\d]+')])
  });

  constructor() {
  }

  ngOnInit() {
  }

  closeDiscountForm() {
    this.closeDiscountFormNotifier.emit();
  }

  onSubmitNewDiscount() {
    let objectToEmit = Object.assign({}, this.obj);
    objectToEmit.discount = this.form.value;

    objectToEmit.discount.start_date = this.convert_YYYY_mm_dd_to_dd_mm_YYYY(this.form.get('start_date').value);
    objectToEmit.discount.finish_date = this.convert_YYYY_mm_dd_to_dd_mm_YYYY(this.form.get('finish_date').value);

    this.submitFormEmitter.emit(objectToEmit);
    this.closeDiscountForm();
  }

  calculateNewPrice() {

  }

  compareDatePickerValues() {

    if ((this.form.get('start_date').value >= this.form.get('finish_date').value)
        && this.form.get('start_date').value !== ''
        && this.form.get('finish_date').value !== '') {
      this.isInputtedDateIncorrect = true;
    }
  }

  convert_YYYY_mm_dd_to_dd_mm_YYYY(stringDate): string {

    let day: string = '', year: string = '', month: string = '';
    let dashCounter: number = 0;
    let counter: number = 0;
    let currentSymbol: string;

    while (counter < stringDate.length) {

      currentSymbol = stringDate.charAt(counter);

      if (dashCounter === 0 && currentSymbol !== '-') {
        year += currentSymbol;
      }

      if (dashCounter === 1 && currentSymbol !== '-') {
        month += currentSymbol;
      }

      if (dashCounter === 2 && currentSymbol !== '-') {
        day += currentSymbol;
      }

      if (stringDate.charAt(counter) === '-') {
        dashCounter++;
      }

      counter++;
    }

    return `${day}-${month}-${year}`;
  }
}
