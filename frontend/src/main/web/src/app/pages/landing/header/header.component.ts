import { Component, OnInit } from '@angular/core';
import * as $ from 'jquery';

import { ApiUserService } from '../../../services/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

	btn: boolean = true;

  constructor(private apiService: ApiUserService) { }

  ngOnInit() {
    $(document).on('click', 'a[href^="#"]', function(e) {
			// target element id
			var id = $(this).attr('href');
    
			// target element
			var $id = $(id);
			if ($id.length === 0) {
				return;
			}
    
			// prevent standard hash navigation (avoid blinking in IE)
			e.preventDefault();
    
			// top position relative to the document
			var pos = $id.offset().top;
    
			// animated top scrolling
			$('body, html').animate({scrollTop: pos});

		});
		this.btn = (localStorage.getItem('rt') === null ? true : false);
		
	}
	
	logOut(){
		this.apiService.logoutUser();
	}

}
