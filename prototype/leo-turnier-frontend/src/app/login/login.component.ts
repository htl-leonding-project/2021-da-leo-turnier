import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  public loginValid = true;
  public username = '';
  public password = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router
  ) {
  }

  public ngOnInit(): void {
  }

  public onSubmit(): void {
    this.loginValid = true;
  }
}
