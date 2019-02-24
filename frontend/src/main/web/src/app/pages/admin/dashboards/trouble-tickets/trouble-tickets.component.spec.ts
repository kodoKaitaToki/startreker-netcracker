import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TroubleTicketsComponent } from './trouble-tickets.component';

describe('TroubleTicketsComponent', () => {
  let component: TroubleTicketsComponent;
  let fixture: ComponentFixture<TroubleTicketsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TroubleTicketsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TroubleTicketsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
