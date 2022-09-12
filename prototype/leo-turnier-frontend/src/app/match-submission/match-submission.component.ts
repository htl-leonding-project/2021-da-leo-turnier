import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {MatchService} from '../services/match.service';
import {Match} from '../model/match.model';
import {Competitor} from '../model/competitor.model';
import {FormControl, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-match-submission',
  templateUrl: './match-submission.component.html',
  styleUrls: ['./match-submission.component.scss']
})
export class MatchSubmissionComponent implements OnInit {
  id = '';
  tournamentId = '';
  match = new Match(0, new Competitor(0, 'comp 1'), new Competitor(0, 'comp 2'), 0, 0, false);

  submitionForm = new FormGroup({
    score1: new FormControl('', [Validators.required]),
    score2: new FormControl('', [Validators.required]),
  });

  constructor(private activatedRouter: ActivatedRoute,
              private router: Router,
              private api: MatchService) {
    // @ts-ignore
    // tslint:disable-next-line:no-shadowed-variable
    activatedRouter.paramMap.subscribe(map => this.id = map.get('id'));
    // @ts-ignore
    // tslint:disable-next-line:no-shadowed-variable
    activatedRouter.paramMap.subscribe( map => this.tournamentId = map.get('tournamentId'));
  }

  async ngOnInit(): Promise<void> {
    this.match = await this.api.getMatch(this.id);
    this.submitionForm.setValue({score1: this.match.score1, score2: this.match.score2});
    console.log(this.match);
    console.log(this.submitionForm.value);
  }

  async onSubmit(): Promise<void> {
    this.match.score1 = this.submitionForm.value.score1;
    this.match.score2 = this.submitionForm.value.score2;

    // @ts-ignore
    const action = document.activeElement.getAttribute('Name');
    console.log(action);

    await this.api.updateMatch(this.match);
    await new Promise(f => setTimeout(f, 100));

    if (action === 'Finish'){
      await this.api.finishMatch(this.id);
    }

    this.router.navigate(['/matches/' + this.tournamentId]).then(() => {
      window.location.reload();
    });
  }
}
