import {Component, OnInit, ViewChild } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Bundle} from '../shared/model/bundle';
import {BundlesService} from "../shared/service/bundles.service";
import {BundlesTableComponent} from "../bundles-table/bundles-table.component";
import {TreeNode} from 'primeng/api';
import {MOCK_TREE} from '../shared/model/mock-tree';

@Component({
             selector: 'app-bundles-component',
             templateUrl: './bundles-component.component.html',
             styleUrls: ['./bundles-component.component.scss']
           },
)
export class BundlesComponentComponent implements OnInit {
  @ViewChild(BundlesTableComponent) table:BundlesTableComponent;

  trips: TreeNode[];

  tripSelected: TreeNode[];


  filterCriteria = [
    {name: 'id'},
    {name: 'price'},
  ];

  filterContent = '';

  currentFilter = this.filterCriteria[0].name;

  currentFilterPlaceholder = `Search by ${this.currentFilter}`;

  form: FormGroup;

  constructor(private bundlesSrvc: BundlesService) {
  }

  ngOnInit(): void {
    this.trips = MOCK_TREE;
    this.form = new FormGroup(
      {
        start_date: new FormControl('', Validators.required),
        finish_date: new FormControl('', Validators.required),
        price: new FormControl('', [Validators.required, Validators.min(0)]),
        description: new FormControl(''),
        trips: new FormControl(''),
        services: new FormControl('')
      }
    );
  }

  chooseNewFilter(chosenFilterName) {

    this.currentFilter = chosenFilterName.value;
    this.currentFilterPlaceholder = `Search by ${this.currentFilter}`;
  }

  onPost() {

    let bundle: Bundle = this.form.value;

    //delete bundles['repeat_password'];

    this.bundlesSrvc.postBundles(bundle)
        .subscribe(() => {
          this.table.getAllBundles();
        }, () => {
          alert('Something went wrong');
        });

    this.form.reset({is_activated: true});
  }

  
}
