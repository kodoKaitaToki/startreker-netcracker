import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ApproverCrudComponent } from './approver-crud.component';

describe('ApproverCrudComponent', () => {
  let component: ApproverCrudComponent;
  let fixture: ComponentFixture<ApproverCrudComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ApproverCrudComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ApproverCrudComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
