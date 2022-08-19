import {APP_INITIALIZER, NgModule} from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from './material-module';
import { TournamentOverviewComponent } from './tournament-overview/tournament-overview.component';
import { LoginComponent } from './login/login.component';
import { PlayerOverviewComponent } from './player-overview/player-overview.component';
import { TeamOverviewComponent } from './team-overview/team-overview.component';
import {HttpClientModule} from '@angular/common/http';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { PlayerComponent } from './player/player.component';
import { TeamComponent } from './team/team.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {TeamOverviewDialogComponent} from './team-overview-dialog/team-overview-dialog.component';
import { DatePipe } from '@angular/common';
import { TeamDialogComponent } from './team-dialog/team-dialog.component';
import { TournamentComponent } from './tournament/tournament.component';
import { HomeComponent } from './home/home.component';
import { TournamentTreeComponent } from './tournament-tree/tournament-tree.component';
import {NgTournamentTreeModule} from 'ng-tournament-tree';
import { MatchComponent } from './match/match.component';
import { TournamentDialogComponent } from './tournament-dialog/tournament-dialog.component';
import {KeycloakAngularModule, KeycloakService} from 'keycloak-angular';
import {initializeKeycloak} from './utility/app.init';
import { MatchOverviewComponent } from './match-overview/match-overview.component';
import { MatchSubmitionComponent } from './match-submition/match-submition.component';
import { TournamentOverviewDialogComponent } from './tournament-overview-dialog/tournament-overview-dialog.component';

@NgModule({
  declarations: [
    AppComponent,
    TournamentOverviewComponent,
    LoginComponent,
    PlayerOverviewComponent,
    TeamOverviewComponent,
    TeamOverviewDialogComponent,
    PlayerComponent,
    TeamComponent,
    TeamDialogComponent,
    TournamentComponent,
    HomeComponent,
    TournamentTreeComponent,
    MatchComponent,
    TournamentDialogComponent,
    MatchOverviewComponent,
    MatchSubmitionComponent,
    TournamentOverviewDialogComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MaterialModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    NgbModule,
    NgTournamentTreeModule,
    KeycloakAngularModule
  ],
  providers: [DatePipe,
    {
      provide: APP_INITIALIZER,
      useFactory: initializeKeycloak,
      multi: true,
      deps: [KeycloakService]
    }],
  bootstrap: [AppComponent]
})
export class AppModule { }
