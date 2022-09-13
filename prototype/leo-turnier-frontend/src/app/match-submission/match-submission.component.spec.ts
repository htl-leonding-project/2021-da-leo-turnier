import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MatchSubmissionComponent } from './match-submission.component';

describe('MatchSubmitionComponent', () => {
  let component: MatchSubmissionComponent;
  let fixture: ComponentFixture<MatchSubmissionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MatchSubmissionComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MatchSubmissionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
