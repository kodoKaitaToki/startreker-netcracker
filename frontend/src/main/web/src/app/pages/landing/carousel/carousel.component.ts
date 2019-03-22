import {Component, OnInit} from '@angular/core';
import { LandingService } from '../../landing/shared/service/landing.service';
import { Bundle } from '../../../shared/model/bundle';

@Component({
  selector: 'carousel-comp',
  templateUrl: './carousel.component.html',
  styleUrls: ['./carousel.component.scss']
})
export class CarouselComponent implements OnInit {

  bundles: Bundle[] = [];

  constructor(private landingService: LandingService) { }

  ngOnInit()  {
    this.landingService.getBundles().subscribe(bundles => this.bundles = bundles);
  }

}
