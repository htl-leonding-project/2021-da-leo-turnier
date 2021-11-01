import {Competitor} from './competitor.model';
import {Tournament} from './tournament.model';
import {Player} from './player.model';

export class Team extends Competitor{
  constructor(public id: number,
              public name: string,
              public totalScore: number,
              public tournaments: Array<Tournament>,
              public players: Array<Player>) {
    super(id, name, totalScore, tournaments);
  }
}
