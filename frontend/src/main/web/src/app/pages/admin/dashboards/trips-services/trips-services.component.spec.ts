import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TripsServicesComponent } from './trips-services.component';

describe('TripsServicesComponent', () => {
  let component: TripsServicesComponent;
  let fixture: ComponentFixture<TripsServicesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TripsServicesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TripsServicesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
