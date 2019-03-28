import { Component, OnInit} from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-trip',
  templateUrl: './trip.component.html',
  styleUrls: ['./trip.component.scss']
})
export class TripComponent implements OnInit {
  constructor(private router: Router) { }

  ngOnInit() {
  }

  handleChange(event){
    let index = event.index;
    let link;
    switch (index) {
      case 0:
        link = ['/approver/trip/open'];
        break;
      case 1:
        link = ['/approver/trip/assigned'];
        break;
      }
      this.router.navigate(link);
  }

}
