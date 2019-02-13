import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  title = 'Startracker';

  user: any = {};

  constructor(private http: HttpClient) {

  }

  ngOnInit()  {

    this.getRequest()
      .subscribe(data => {
        this.user = data;
      })
  }

  getRequest() {

    return this.http.get('http://localhost:8090/api');
  }
}
