import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

@Component({
  selector: 'app-search-result',
  templateUrl: './search-result.component.html',
  styleUrls: ['./search-result.component.scss']
})
export class SearchResultComponent implements OnInit {
  searchForm: FormGroup;

  constructor() {
  }

  ngOnInit() {
    this.searchForm = new FormGroup({});
  }

  onSubmit(){
  }
}
