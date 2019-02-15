import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BundlesCrudComponent } from './bundles-crud.component';

describe('BundlesCrudComponent', () => {
  let component: BundlesCrudComponent;
  let fixture: ComponentFixture<BundlesCrudComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BundlesCrudComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BundlesCrudComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
