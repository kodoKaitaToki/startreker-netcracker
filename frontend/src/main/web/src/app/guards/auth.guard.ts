import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { ApiUserService } from '../services/auth.service';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {

  constructor(private router: Router, private authenticationService: ApiUserService) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
      const currentUser = this.authenticationService.userInfo;
      if (currentUser) {
          // check if route is restricted by role
              // role not authorised so redirect to home page
            if (route.data.roles && ((currentUser.authorities.length == 0)||(route.data.roles.indexOf(currentUser.authorities[0].authority) === -1))) {
              this.router.navigate(['/']);
              return false;
          }

          // authorised so return true
          return true;
    }

    // not logged in so redirect to login page with the return url
    this.router.navigate(['/login']);
    return false;
  }
}
