import {SportType} from './sport-type.model';
import {TournamentMode} from './tournament-mode.model';

export class TournamentDTO {
  constructor(public id: number,
              public name: string,
              public startDate: Date,
              public endDate: Date,
              public sportType: SportType,
              public tournamentMode: TournamentMode) {
  }
}
