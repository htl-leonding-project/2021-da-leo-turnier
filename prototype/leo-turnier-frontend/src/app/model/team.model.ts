import {Competitor} from './competitor.model';
import {Player} from './player.model';

export class Team extends Competitor{
  constructor(public id: number,
              public name: string,
              public players: Array<Player>) {
    super(id, name);
  }
}
