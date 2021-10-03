import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MaterialModule} from "./material-module";
import { CreateTournamentComponent } from './create-tournament/create-tournament.component';
import { TournamentOverviewComponent } from './tournament-overview/tournament-overview.component';
import { LoginComponent } from './login/login.component';
import { CompetitorOverviewComponent } from './competitor-overview/competitor-overview.component';
import { TeamOverviewComponent } from './team-overview/team-overview.component';

@NgModule({
  declarations: [
    AppComponent,
    CreateTournamentComponent,
    TournamentOverviewComponent,
    LoginComponent,
    CompetitorOverviewComponent,
    TeamOverviewComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MaterialModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
