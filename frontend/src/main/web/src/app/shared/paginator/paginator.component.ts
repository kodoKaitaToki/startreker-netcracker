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

  nextBtn: boolean = false;

  pageArray = Array;

  count() : number {
    return Math.floor(this.amount/this.number)
  }

  checkLastPage(){
    if(this.amount > -1){
      this.nextBtn = (Math.floor(this.amount/this.from) <= 1) ? true:false;
    }
  }

  first() {
    this.from = 0;
    this.update.emit(this.from);
  }

  next() {
    this.from += this.number;
    this.update.emit(this.from);
    this.checkLastPage();
  }

  previous() {
    this.from -= this.number;
    this.nextBtn = false;
    if (this.from < 0) {
      this.from = 0;
    }
    this.update.emit(this.from);
  }

  goToPage(page: number) {
    this.from = page * this.number;
    this.update.emit(this.from);
  }
}