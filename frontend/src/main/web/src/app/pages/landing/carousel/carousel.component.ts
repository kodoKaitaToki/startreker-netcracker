import {Component, OnInit} from '@angular/core';
import { LandingService } from '../../landing/shared/service/landing.service';

@Component({
  selector: 'carousel-comp',
  templateUrl: './carousel.component.html',
  styleUrls: ['./carousel.component.scss']
})
export class CarouselComponent implements OnInit {

  bundles = [];

  constructor(private landingService: LandingService) { }

  ngOnInit()  {
    this.landingService.getBundles().subscribe(bundles => this.bundles = bundles);
  }

}
