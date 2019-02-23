import { Component,  
        Input, 
        Output,
        OnInit, 
        EventEmitter} from '@angular/core';
import {Carrier} from '../carrier';
import {UpdCarrier} from '../updCarrier';
import {FormControl, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-carrier-table',
  templateUrl: './carrier-table.component.html',
  styleUrls: ['./carrier-table.component.scss']
})
export class CarrierTableComponent implements OnInit {

  @Input() carriers: Carrier;
  @Input() allCarriers: Carrier[] = [];
  @Input() numpages: Number;
  @Input() butGroup = {};

  @Input() filterCriteria: string;
  @Input() filterContent: string;

  @Output() 
  notifyAboutDelete: EventEmitter<number> = new EventEmitter<number>();

  @Output() 
  notifyAboutUpdate: EventEmitter<UpdCarrier> = new EventEmitter<UpdCarrier>();

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
        tel: new FormControl('', [Validators.required, Validators.pattern('[0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]')]),
        status: new FormControl('on')
      }
    );
  }

  onUpdate(event) {

    this.currentCarrierForUpdate = event;
    this.isEditButtonBlockedAfterSubmit = true;
    this.editBut = true;

    let status: string;
     if(this.currentCarrierForUpdate.user_is_activated == true){
       status = 'on';
     }else{
       status = 'off';
     }

    this.form = new FormGroup(
      {
        email: new FormControl(this.currentCarrierForUpdate.user_email, [Validators.required, Validators.email]),
        name: new FormControl(this.currentCarrierForUpdate.username, Validators.required),
        tel: new FormControl(this.currentCarrierForUpdate.user_telephone, [Validators.required, Validators.pattern('[0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]')]),
        status: new FormControl(status)
      }
    );

  }

  onDelete(id: number) {
    this.notifyAboutDelete.emit(id);
  }

  onSubmit(id: number) {
    
    let updUser: UpdCarrier;
    updUser = {user_id: id,
              username: this.form.value['name'],
              email: this.form.value['email'],
              telephone_number: this.form.value['tel'],
              is_activated: true
            };

    this.notifyAboutUpdate.emit(updUser);
            
    this.editBut = false;
  }

  onChange(event: number){
    this.page = event;
    this.notifyAboutChangePage.emit(event);
  }

}
