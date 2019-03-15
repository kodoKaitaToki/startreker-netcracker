import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-paginator',
  templateUrl: './paginator.component.html',
  styleUrls: ['./paginator.component.scss']
})
export class PaginatorComponent {
  @Input() page: Array<any>;
  @Input() amount: number = -1;
  @Input() from: number;
  @Input() number: number;
  @Output() update = new EventEmitter<number>();

  pageArray = Array;

  count() : number {
    return Math.floor(this.amount/this.number)
  }

  first() {
    this.from = 0;
    this.update.emit(this.from);
  }

  next() {
    this.from += this.number;
    this.update.emit(this.from);
  }

  previous() {
    this.from -= this.number;
    if (this.from < 0) this.from = 0;
    this.update.emit(this.from);
  }

  goToPage(page: number) {
    this.from = page * this.number;
    this.update.emit(this.from);
  }
}