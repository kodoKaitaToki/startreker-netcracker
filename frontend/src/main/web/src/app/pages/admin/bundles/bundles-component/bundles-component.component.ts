import {Component, OnInit, ViewChild } from '@angular/core';
import {FormControl, FormGroup, FormArray, Validators, FormBuilder} from '@angular/forms';
import {Bundle} from '../shared/model/bundle';
import {BundleForm} from '../shared/model/bundle-form';
import {BundlesService} from "../shared/service/bundles.service";
import {BundlesTableComponent} from "../bundles-table/bundles-table.component";
import {BundlesTreeComponent} from "../bundles-tree/bundles-tree.component";
import {MessageService} from "primeng/api";
import {TreeNode} from 'primeng/api';
import { Trip } from '../shared/model/trip';
import { TicketClass } from '../shared/model/ticket-class';
import { Service } from '../shared/model/service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
             selector: 'app-bundles-component',
             templateUrl: './bundles-component.component.html',
             styleUrls: ['./bundles-component.component.scss']
           },
)
export class BundlesComponentComponent implements OnInit {
  @ViewChild(BundlesTableComponent) table:BundlesTableComponent;

  constructor(
    private bundlesSrvc: BundlesService,
    private messageService: MessageService) {
  }

  ngOnInit(): void {
  }

  onPost(bundleForm: BundleForm) {
    console.log(bundleForm);

    this.bundlesSrvc.postBundle(bundleForm)
        .subscribe(() => {
          this.showMessage(this.createMessage('success', 'Bundle post', 'The bundle was created'));
          this.table.getAllBundles();
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
