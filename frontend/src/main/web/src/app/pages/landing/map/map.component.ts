import { Component, OnInit} from '@angular/core';

declare function main(): any;

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.scss']
})
export class MapComponent implements OnInit {


  constructor() {
  }

  ngOnInit() {
    main();
  }

}
