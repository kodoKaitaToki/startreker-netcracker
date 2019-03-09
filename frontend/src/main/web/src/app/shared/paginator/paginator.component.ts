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
  @Output() update = new EventEmitter<{from: number, number: number}>();

  next() {
    this.from += this.number;
    this.update.emit({from: this.from, number: this.number})
  }
}