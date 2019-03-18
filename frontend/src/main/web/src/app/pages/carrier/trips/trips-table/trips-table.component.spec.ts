import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TripsTableComponent } from './trips-table.component';

describe('TripsTableComponent', () => {
  let component: TripsTableComponent;
  let fixture: ComponentFixture<TripsTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TripsTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TripsTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
