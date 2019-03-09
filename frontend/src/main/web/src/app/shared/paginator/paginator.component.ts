import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-paginator',
  templateUrl: './paginator.component.html',
  styleUrls: ['./paginator.component.scss']
})
export class PaginatorComponent {
  @Input() page: Array<any>;
  @Input() from: number;
  @Input() number: number;
  @Output() update = new EventEmitter<number>();

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
}