import {Competitor} from './competitor.model';

export class Participation {
  constructor(public competitor: Competitor,
              public placement: number){
  }
}
