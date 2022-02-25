import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TeamOverviewDialogComponent } from './team-overview-dialog.component';

describe('TeamOverviewDialogComponent', () => {
  let component: TeamOverviewDialogComponent;
  let fixture: ComponentFixture<TeamOverviewDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TeamOverviewDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TeamOverviewDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
