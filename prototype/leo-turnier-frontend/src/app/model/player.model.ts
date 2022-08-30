import {Competitor} from './competitor.model';
import {Team} from './team.model';

export class Player extends Competitor{
  constructor(public id: number,
              public name: string,
              public birthdate: Date,
              public team: Team) {
    super(id, name);
  }
}
