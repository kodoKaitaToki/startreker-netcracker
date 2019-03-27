import {Component, OnInit} from '@angular/core';
import { LandingService } from '../../landing/shared/service/landing.service';
import { Bundle } from '../../../shared/model/bundle';
import { Router } from '@angular/router';

@Component({
  selector: 'carousel-comp',
  templateUrl: './carousel.component.html',
  styleUrls: ['./carousel.component.scss']
})
export class CarouselComponent implements OnInit {

  bundles: Bundle[] = [];

  constructor(private landingService: LandingService,
              private router: Router) { }

  ngOnInit()  {
    this.landingService.getBundles().subscribe(bundles => this.bundles = bundles);
  }

  buy(id: number){
    let bundles: number[]= new Array();
    let stContent = JSON.parse(sessionStorage.getItem('bundles'));
    if(stContent !== null){
      if(stContent.length > 0){
        for(let bundle of stContent){
          bundles.push(bundle);
        }
      }
    }
    bundles.push(id);
    sessionStorage.setItem('bundles', JSON.stringify(bundles));

    if (localStorage.getItem('at') !== null) {
      this.router.navigate(['user/cart']);
    } else {
      this.router.navigate(["login"], {state: {message: 'Please, log in or sign up to proceed!'}});
    }
  }

}
