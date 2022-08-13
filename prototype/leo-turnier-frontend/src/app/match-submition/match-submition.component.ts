import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {MatchService} from '../services/match.service';
import {Match} from '../model/match.model';
import {Competitor} from '../model/competitor.model';
import {map} from 'rxjs/operators';
import {Location} from '@angular/common';
import {FormControl, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-match-submition',
  templateUrl: './match-submition.component.html',
  styleUrls: ['./match-submition.component.scss']
})
export class MatchSubmitionComponent implements OnInit {
  id = '';
  match = new Match(0, new Competitor(0, 'comp 1'), new Competitor(0, 'comp 2'), 0, 0, false);

  submitionForm = new FormGroup({
    score1: new FormControl('', [Validators.required]),
    score2: new FormControl('', [Validators.required]),
  });

  constructor(private activatedRouter: ActivatedRoute,
              private location: Location,
              private api: MatchService) {
    // @ts-ignore
    // tslint:disable-next-line:no-shadowed-variable
    activatedRouter.paramMap.subscribe(map => this.id = map.get('id'));
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

    this.location.back();
  }
}
