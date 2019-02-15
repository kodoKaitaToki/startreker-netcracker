import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CarrierCrudComponent } from './carrier-crud.component';

describe('CarrierCrudComponent', () => {
  let component: CarrierCrudComponent;
  let fixture: ComponentFixture<CarrierCrudComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CarrierCrudComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CarrierCrudComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
