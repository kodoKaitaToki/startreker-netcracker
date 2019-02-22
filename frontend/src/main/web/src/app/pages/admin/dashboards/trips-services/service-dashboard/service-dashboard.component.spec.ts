import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ServiceDashboardComponent } from './service-dashboard.component';

describe('ServiceDashboardComponent', () => {
  let component: ServiceDashboardComponent;
  let fixture: ComponentFixture<ServiceDashboardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ServiceDashboardComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ServiceDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
