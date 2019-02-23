import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Approver} from '../shared/model/approver';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {ApproverService} from "../shared/services/approver.service";

@Component({
  selector: 'app-approver-table',
  templateUrl: './approver-table.component.html',
  styleUrls: ['./approver-table.component.scss']
})
export class ApproverTableComponent implements OnInit {

  @Input() approvers: Approver[];
  @Input() filterCriteria: string;
  @Input() filterContent: string;

  currentApproverForUpdate = new Approver();
  approverId: number;
  isForUpdateMessage = false;

  form: FormGroup;
  isEditButtonBlockedAfterSubmit = true;

  totalRec: number;
  page: number = 1;
  entriesAmountOnPage = 10;

  @Output() emittedApprovers = new EventEmitter<any[]>();

  constructor(private approverService: ApproverService) {

  }

  ngOnInit() {

    this.setFormInDefault();
    this.totalRec = this.approvers.length;
  }

  setFormInDefault() {

    this.form = new FormGroup(
      {
        user_email: new FormControl('', [Validators.required, Validators.email]),
        username: new FormControl('', Validators.required),
        user_telephone: new FormControl('', Validators.required),
        user_is_activated: new FormControl(false, Validators.required),
      }
    );
  }

  onUpdate(event) {

    this.approverId = event.id;
    this.isEditButtonBlockedAfterSubmit = true;
    this.currentApproverForUpdate = event;

    console.log(this.currentApproverForUpdate);

    this.form = new FormGroup(
      {
        user_email: new FormControl(this.currentApproverForUpdate.user_email, [Validators.required, Validators.email]),
        username: new FormControl(this.currentApproverForUpdate.user_email, Validators.required),
        user_telephone: new FormControl(this.currentApproverForUpdate.user_telephone, Validators.required),
        user_is_activated: new FormControl(this.currentApproverForUpdate.user_is_activated, Validators.required)
      }
    );
  }

  onDelete(event) {

    // this.approverService.deleteApprover(event).subscribe(
    //   () => {
    //     this.approverService.getAll()
    //       .subscribe((data) => {
    //         this.emittedApprovers.emit(data);
    //       })
    //   }
    // );

    console.log(event);
  }

  onSubmit() {

    this.isForUpdateMessage = true;
    this.isEditButtonBlockedAfterSubmit = false;

    this.currentApproverForUpdate = this.form.value;
    this.currentApproverForUpdate.id = this.approverId;

    console.log(this.currentApproverForUpdate);

    // this.approverService.putApprover(this.currentApproverForUpdate)
    //   .subscribe(() => {
    //     this.isForUpdateMessage = false;
    //     this.currentApproverForUpdate = new Approver();
    //
    //     this.approverService.getAll()
    //       .subscribe((data) => {
    //         this.emittedApprovers.emit(data);
    //       })
    //   });
  }

  onChangePage($event) {

    this.page = $event;
    window.scrollTo(0, 0);
  }
}
