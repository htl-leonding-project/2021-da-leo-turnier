import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {CreateTournamentComponent} from './create-tournament/create-tournament.component';
import {TournamentOverviewComponent} from './tournament-overview/tournament-overview.component';
import {LoginComponent} from './login/login.component';
import {CompetitorOverviewComponent} from './competitor-overview/competitor-overview.component';
import {TeamOverviewComponent} from './team-overview/team-overview.component';

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'tournament-overview'},
  { path: 'tournament-overview', component: TournamentOverviewComponent},
  { path: 'competitor-overview', component: CompetitorOverviewComponent},
  { path: 'team-overview', component: TeamOverviewComponent},
  { path: 'login', component: LoginComponent},
  { path: 'create-tournament', component: CreateTournamentComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
