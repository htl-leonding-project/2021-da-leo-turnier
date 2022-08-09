import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MatchSubmitionComponent } from './match-submition.component';

describe('MatchSubmitionComponent', () => {
  let component: MatchSubmitionComponent;
  let fixture: ComponentFixture<MatchSubmitionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MatchSubmitionComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MatchSubmitionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
