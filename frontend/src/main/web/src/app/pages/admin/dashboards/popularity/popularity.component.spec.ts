import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PopularityComponent } from './popularity.component';

describe('PopularityComponent', () => {
  let component: PopularityComponent;
  let fixture: ComponentFixture<PopularityComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PopularityComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PopularityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
