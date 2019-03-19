import { Component, OnInit } from '@angular/core';

import { ApiUserService } from '../../../services/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

  constructor(private apiService: ApiUserService) { }

  ngOnInit() {
  }

  logOut(){
		this.apiService.logoutUser();
	}

}
