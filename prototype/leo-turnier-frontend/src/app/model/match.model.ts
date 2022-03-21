import {Competitor} from './competitor.model';

export class Match{
  constructor(public id: number,
              public competitor1: Competitor,
              public competitor2: Competitor,
              public score1: number,
              public score2: number) {
  }
}
