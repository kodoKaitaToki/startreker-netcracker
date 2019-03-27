import { Component, OnInit } from '@angular/core';
import { ApiUserService } from '../../../services/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './app-header.component.html',
  styleUrls: ['./app-header.component.scss']
})
export class AppHeaderComponent implements OnInit {

  btn: boolean = true;

  constructor(private apiService: ApiUserService) { }

  ngOnInit() {this.btn = (localStorage.getItem('rt') === null ? true : false);}

  logOut(){
    this.apiService.logoutUser();
  }

}
