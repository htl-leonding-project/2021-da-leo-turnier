import {SportType} from './sport-type.model';
import {TournamentMode} from './tournament-mode.model';
import {Competitor} from './competitor.model';

export class Tournament {
  constructor(public id: number,
              public name: string,
              public startDate: Date,
              public endDate: Date,
              public sportType: SportType,
              public tournamentMode: TournamentMode,
              public competitors: Competitor[],
              public isTournamentRunning: boolean,
              public isFinished: boolean) {
  }
}
