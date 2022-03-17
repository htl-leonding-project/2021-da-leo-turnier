import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TournamentTreeComponent } from './tournament-tree.component';

describe('TournamentTreeComponent', () => {
  let component: TournamentTreeComponent;
  let fixture: ComponentFixture<TournamentTreeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TournamentTreeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TournamentTreeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
