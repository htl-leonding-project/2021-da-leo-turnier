import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {TournamentOverviewComponent} from './tournament-overview/tournament-overview.component';
import {LoginComponent} from './login/login.component';
import {PlayerOverviewComponent} from './player-overview/player-overview.component';
import {TeamOverviewComponent} from './team-overview/team-overview.component';
import {PlayerComponent} from './player/player.component';
import {TeamComponent} from './team/team.component';
import {TournamentComponent} from './tournament/tournament.component';
import {HomeComponent} from './home/home.component';
import {TournamentTreeComponent} from './tournament-tree/tournament-tree.component';
import {AuthGuard} from './utility/app.guard';
import {MatchOverviewComponent} from './match-overview/match-overview.component';
import {MatchSubmissionComponent} from './match-submission/match-submission.component';
import {TournamentTableComponent} from './tournament-table/tournament-table.component';

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'home'},
  { path: 'home', component: HomeComponent},
  { path: 'tournaments', component: TournamentOverviewComponent},
  { path: 'tournaments/:id', component: TournamentComponent},
  { path: 'tournament-tree', component: TournamentTreeComponent},
  { path: 'tournament-table/:id', component: TournamentTableComponent},
  { path: 'players', component: PlayerOverviewComponent},
  { path: 'players/:id', component: PlayerComponent},
  { path: 'team', component: TeamOverviewComponent},
  { path: 'team/:id', component: TeamComponent},
  { path: 'matches/:id', component: MatchOverviewComponent},
  { path: 'match-submission/:id/:tournamentId', component: MatchSubmissionComponent},
  { path: 'login', component: LoginComponent, canActivate: [AuthGuard]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
