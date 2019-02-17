import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Approver} from '../shared/model/approver';

@Component({
  selector: 'app-approver-table',
  templateUrl: './approver-table.component.html',
  styleUrls: ['./approver-table.component.scss']
})
export class ApproverTableComponent implements OnInit {

  @Input() approvers: Approver;

  @Input() filterCriteria: string;
  @Input() filterContent: string;

  @Output() notifyAboutUpdate = new EventEmitter();
  @Output() notifyAboutDelete = new EventEmitter();

  constructor() {
  }

  ngOnInit() {

  }

  onUpdate(event) {

    this.notifyAboutUpdate.emit(event);
  }

  onDelete() {

    this.notifyAboutDelete.emit();
  }

}
