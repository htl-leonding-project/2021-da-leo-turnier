import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {CreateTournamentComponent} from './create-tournament/create-tournament.component';
import {TournamentOverviewComponent} from './tournament-overview/tournament-overview.component';
import {LoginComponent} from './login/login.component';
import {PlayerOverviewComponent} from './player-overview/player-overview.component';
import {TeamOverviewComponent} from './team-overview/team-overview.component';
import {PlayerComponent} from './player/player.component';
import {TeamComponent} from './team/team.component';

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'tournament-overview'},
  { path: 'tournament-overview', component: TournamentOverviewComponent},
  { path: 'players', component: PlayerOverviewComponent},
  { path: 'players/:id', component: PlayerComponent},
  { path: 'team', component: TeamOverviewComponent},
  { path: 'team/:id', component: TeamComponent},
  { path: 'login', component: LoginComponent},
  { path: 'create-tournament', component: CreateTournamentComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
