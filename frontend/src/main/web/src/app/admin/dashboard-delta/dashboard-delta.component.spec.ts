import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardDeltaComponent } from './dashboard-delta.component';

describe('DashboardDeltaComponent', () => {
  let component: DashboardDeltaComponent;
  let fixture: ComponentFixture<DashboardDeltaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DashboardDeltaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DashboardDeltaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
