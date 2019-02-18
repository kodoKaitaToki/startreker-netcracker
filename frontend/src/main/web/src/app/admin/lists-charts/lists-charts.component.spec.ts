import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ListsChartsComponent } from './lists-charts.component';

describe('ListsChartsComponent', () => {
  let component: ListsChartsComponent;
  let fixture: ComponentFixture<ListsChartsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ListsChartsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ListsChartsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
