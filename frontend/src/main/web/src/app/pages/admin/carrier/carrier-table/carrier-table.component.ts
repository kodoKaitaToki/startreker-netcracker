import { Component,  
        Input, 
        Output,
        OnInit, 
        EventEmitter} from '@angular/core';
import {Carrier} from '../carrier';
import {FormControl, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-carrier-table',
  templateUrl: './carrier-table.component.html',
  styleUrls: ['./carrier-table.component.scss']
})
export class CarrierTableComponent implements OnInit {

  @Input() carriers: Carrier;
  @Input() allCarriers: Carrier[];
  @Input() numpages: Number;
  @Input() butGroup = {};
  @Input() updateError;

  @Input() filterCriteria: string;
  @Input() filterContent: string;

  @Output() 
  notifyAboutDelete: EventEmitter<number> = new EventEmitter<number>();

  @Output() 
  notifyAboutUpdate = new EventEmitter();

  @Output() 
  notifyAboutChangePage: EventEmitter<number> = new EventEmitter<number>();

  currentCarrierForUpdate = new Carrier();
  isForUpdateMessage = false;
  page: number = 1;

  form: FormGroup;
  isEditButtonBlockedAfterSubmit = true;
  editBut = false;

  constructor() {}

  ngOnInit() {
    this.setFormInDefault();
  }

  setFormInDefault() {

    this.form = new FormGroup(
      {
        email: new FormControl('', [Validators.required, Validators.email]),
        name: new FormControl('', Validators.required),
        telephone_number: new FormControl('', [Validators.required, Validators.pattern('[0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]')]),
        is_activated: new FormControl('on')
      }
    );
  }

  onUpdate(event) {

    this.currentCarrierForUpdate = event;
    this.isEditButtonBlockedAfterSubmit = true;
    this.editBut = true;

    let status: string;
     if(this.currentCarrierForUpdate.is_activated == true){
       status = 'on';
     }else{
       status = 'off';
     }

    this.form = new FormGroup(
      {
        email: new FormControl(this.currentCarrierForUpdate.email, [Validators.required, Validators.email]),
        username: new FormControl(this.currentCarrierForUpdate.username, Validators.required),
        telephone_number: new FormControl(this.currentCarrierForUpdate.telephone_number, [Validators.required, Validators.pattern('[0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]')]),
        is_activated: new FormControl(status)
      }
    );

  }

  onDelete(id: number) {
    this.notifyAboutDelete.emit(id);
  }

  onSubmit() {
    this.form.value.id = this.currentCarrierForUpdate.id;
    let status: boolean;
    if(this.form.value['is_activated'] == 'on'){
      status = true;
    }else{
      status = false;
    }

    let event = {id: this.currentCarrierForUpdate.id,
                username: this.form.value['username'],
                email: this.form.value['email'],
                telephone_number: this.form.value['telephone_number'],
                is_activated: status,
                user_created_date: this.form.value['user_created_date']
    }

    this.notifyAboutUpdate.emit(event);
            
    this.editBut = false;
  }

  onChange(event: number){
    this.page = event;
    this.notifyAboutChangePage.emit(event);
  }

}
