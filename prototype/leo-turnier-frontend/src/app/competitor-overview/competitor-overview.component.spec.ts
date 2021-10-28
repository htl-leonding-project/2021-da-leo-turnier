import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CompetitorOverviewComponent } from './competitor-overview.component';

describe('CompetitorOverviewComponent', () => {
  let component: CompetitorOverviewComponent;
  let fixture: ComponentFixture<CompetitorOverviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CompetitorOverviewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CompetitorOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
