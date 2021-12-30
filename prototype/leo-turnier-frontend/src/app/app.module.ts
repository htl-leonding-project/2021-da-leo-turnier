import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MaterialModule} from './material-module';
import { CreateTournamentComponent } from './create-tournament/create-tournament.component';
import { TournamentOverviewComponent } from './tournament-overview/tournament-overview.component';
import { LoginComponent } from './login/login.component';
import { PlayerOverviewComponent } from './player-overview/player-overview.component';
import { TeamOverviewComponent } from './team-overview/team-overview.component';
import {HttpClientModule} from '@angular/common/http';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { PlayerComponent } from './player/player.component';

@NgModule({
  declarations: [
    AppComponent,
    CreateTournamentComponent,
    TournamentOverviewComponent,
    LoginComponent,
    PlayerOverviewComponent,
    TeamOverviewComponent,
    PlayerComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MaterialModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
