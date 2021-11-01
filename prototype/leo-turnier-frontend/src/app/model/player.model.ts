// tslint:disable-next-line:max-line-length
// Note for Production: if Array as List doesn't work try https://stackoverflow.com/questions/23096260/is-there-a-typescript-list-and-or-map-class-library

import {Tournament} from './tournament.model';
import {Competitor} from './competitor.model';
import {Team} from './team.model';

export class Player extends Competitor{
  constructor(public id: number,
              public name: string,
              public totalScore: number,
              public tournaments: Array<Tournament>,
              public birthdate: Date,
              public team: Team) {
    super(id, name, totalScore, tournaments);
  }
}
