import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {ReactiveFormsModule} from '@angular/forms';

@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.scss'],
})
export class HistoryComponent implements OnInit {

  public searchParams: FormGroup;

  constructor() {
  }

  ngOnInit() {
    this.searchParams = new FormGroup({
      testparam: new FormControl("")
    });
  }

  search(data) {
    console.log(data);
  }

}
