import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {KeycloakService} from 'keycloak-angular';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  public username = '';
  public role = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private keycloakService: KeycloakService
  ) {
  }

  public ngOnInit(): void {
    this.initializeUserOptions();
  }

  private initializeUserOptions(): void {
    this.username = this.keycloakService.getUsername();
    // @ts-ignore
    this.role = this.keycloakService.getUserRoles().pop();

    console.log(this.username);
  }
}
