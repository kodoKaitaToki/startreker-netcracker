import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TripsTicketClassComponent } from './trips-ticket-class.component';

describe('TripsTicketClassComponent', () => {
  let component: TripsTicketClassComponent;
  let fixture: ComponentFixture<TripsTicketClassComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TripsTicketClassComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TripsTicketClassComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
