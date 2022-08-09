import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {KeycloakService} from 'keycloak-angular';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit{
  isLoggedIn = false;

  constructor(public router: Router,
              private keycloakService: KeycloakService) {
  }

  onMenuSelected(page: string): void{
    this.router.navigate(['/' + page]);
  }

  async ngOnInit(): Promise<void> {
    this.isLoggedIn = await this.keycloakService.isLoggedIn();
    console.log(this.isLoggedIn);
    console.log(this.keycloakService.getUsername());
  }
}
