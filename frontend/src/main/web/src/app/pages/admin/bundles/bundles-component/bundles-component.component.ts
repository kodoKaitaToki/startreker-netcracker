import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Bundles} from '../shared/model/bundles';
import {BundlesService} from "../shared/service/bundles.service";

@Component({
             selector: 'app-bundles-component',
             templateUrl: './bundles-component.component.html',
             styleUrls: ['./bundles-component.component.scss']
           },
)
export class BundlesComponentComponent implements OnInit {

  bundles: Bundles[] = [];


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

    this.getAllBundles();
  }

  chooseNewFilter(chosenFilterName) {

    this.currentFilter = chosenFilterName.value;
    this.currentFilterPlaceholder = `Search by ${this.currentFilter}`;
  }

  onPost() {

    let bundles: Bundles = this.form.value;

    //delete bundles['repeat_password'];

    this.bundlesSrvc.postBundles(bundles)
        .subscribe(() => {
          this.getAllBundles();
        }, () => {
          alert('Something went wrong');
        });

    this.form.reset({is_activated: true});
  }

  getBundlesForUpdate(bundles) {

    this.bundlesSrvc.putBundles(bundles)
        .subscribe(() => {
          this.getAllBundles();
        }, () => {
          alert('Something went wrong');
        });
  }

  getBundlesForDelete(bundles) {

    this.bundlesSrvc.deleteBundles(bundles)
        .subscribe(() => {
          this.getAllBundles();
        });
  }

  getAllBundles() {

    this.bundlesSrvc.getAll()
        .subscribe(data => this.bundles = data);
  }

/*  checkRepeatedPasswordTheSame() {
    return this.form.get('password').value === this.form.get('repeat_password').value;
  } */
}
