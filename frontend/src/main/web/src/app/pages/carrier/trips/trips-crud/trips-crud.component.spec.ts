import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TripsCrudComponent } from './trips-crud.component';

describe('TripsCrudComponent', () => {
  let component: TripsCrudComponent;
  let fixture: ComponentFixture<TripsCrudComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TripsCrudComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TripsCrudComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
