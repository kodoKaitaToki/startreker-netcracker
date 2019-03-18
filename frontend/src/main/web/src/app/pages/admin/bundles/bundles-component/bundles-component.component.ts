import {Component, OnInit, ViewChild } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Bundle} from '../shared/model/bundle';
import {BundlesService} from "../shared/service/bundles.service";
import {BundlesTableComponent} from "../bundles-table/bundles-table.component";
import {MessageService} from "primeng/api";
import {TreeNode} from 'primeng/api';
import {MOCK_TREE} from '../shared/model/mock-tree';
import {Carrier} from '../../carrier/carrier';
import {CarrierCrudService} from '../../carrier/carrier-crud.service';
import {HttpErrorResponse} from "@angular/common/http";
import { HttpResponse } from '@angular/common/http';

@Component({
             selector: 'app-bundles-component',
             templateUrl: './bundles-component.component.html',
             styleUrls: ['./bundles-component.component.scss']
           },
)
export class BundlesComponentComponent implements OnInit {
  @ViewChild(BundlesTableComponent) table:BundlesTableComponent;

  inputTree: TreeNode[];

  selectedTree: TreeNode[];

  filterCriteria = [
    {name: 'id'},
    {name: 'price'},
  ];

  filterContent = '';

  currentFilter = this.filterCriteria[0].name;

  currentFilterPlaceholder = `Search by ${this.currentFilter}`;

  form: FormGroup;

  constructor(
    private bundlesSrvc: BundlesService,
    private carrierSrvc: CarrierCrudService,
    private messageService: MessageService) {
  }

  ngOnInit(): void {
    this.populateCarriers();
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

  chooseNewFilter(chosenFilterName: { value: string; }) {

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

  populateCarriers() {
    this.inputTree = [];
    this.carrierSrvc.getAllCarriers()
    .subscribe((resp: HttpResponse<any>) => {
      this.inputTree = this.inputTree.concat(
        resp.body.map((carrier: { username: string; }) => {
          let node: TreeNode = {
            label: carrier.username,
            selectable: false
          }
          return node;
      }))
    }, (error: HttpErrorResponse) => {
      this.showMessage(this.createMessage('error', `Error message - ${error.error.status}`, error.error.error));
    });
  }

  createMessage(severity: string, summary: string, detail: string): any {
    return {
      severity: severity,
      summary: summary,
      detail: detail
    };
  }

  showMessage(msgObj: any) {
    this.messageService.add(msgObj);
  }

  
}
