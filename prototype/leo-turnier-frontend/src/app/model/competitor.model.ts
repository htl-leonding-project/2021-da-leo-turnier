import {Tournament} from './tournament.model';

export class Competitor{
  constructor(public id: number,
              public name: string,
              public totalScore: number,
              public tournaments: Array<Tournament>) {
  }
}
