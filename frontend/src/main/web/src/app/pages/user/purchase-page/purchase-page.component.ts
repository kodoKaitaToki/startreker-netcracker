import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-purchase-page',
  templateUrl: './purchase-page.component.html',
  styleUrls: ['./purchase-page.component.scss']
})
export class PurchasePageComponent implements OnInit {

  // services: Service[] = [];

  tickets = [];

  constructor() { }

  ngOnInit() { }
}
