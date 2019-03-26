import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { ApiUserService } from '../../services/auth.service';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {

  constructor(private router: Router, private authenticationService: ApiUserService) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot){
      const currentUser = JSON.parse(localStorage.getItem('userdata'));
      if (currentUser) {
          for(let role of currentUser.roles){
            if(route.data.roles == role) return true;
          };
          this.router.navigate(['/notFound']);
          return false;
      }

    // not logged in so redirect to login page with the return url
    this.router.navigate(['/login']);
    return false;
  }
}
