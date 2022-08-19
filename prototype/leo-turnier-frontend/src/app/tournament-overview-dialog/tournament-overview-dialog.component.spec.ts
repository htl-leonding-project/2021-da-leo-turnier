import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TournamentOverviewDialogComponent } from './tournament-overview-dialog.component';

describe('TournamentOverviewDialogComponent', () => {
  let component: TournamentOverviewDialogComponent;
  let fixture: ComponentFixture<TournamentOverviewDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TournamentOverviewDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TournamentOverviewDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
