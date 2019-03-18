import { Component, OnInit, Input, Output } from '@angular/core';
import { Trip } from 'src/app/pages/admin/bundles/shared/model/trip';
import { PossibleServiceService } from 'src/app/services/possible-service.service';
import { TreeNode } from 'primeng/api';

@Component({
  selector: 'app-suggestions-tree',
  templateUrl: './suggestions-tree.component.html',
  styleUrls: ['./suggestions-tree.component.scss']
})
export class SuggestionsTreeComponent implements OnInit {
  inputTree: TreeNode[];

  selectedTree: TreeNode[];

  loading: boolean;

  @Input() trip: Trip;

  //@Output() selectedServices = new EventEmitter<Service[]>();

  constructor() { }

  ngOnInit() {
  }

}
