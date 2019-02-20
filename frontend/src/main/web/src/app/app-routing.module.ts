import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LandingComponent } from './pages/landing/landing.component';
import { NotFoundPageComponent } from './pages/not-found-page/not-found-page.component';
import { LoginComponent } from './pages/login/login.component';
import { RegistrationComponent } from './pages/registration/registration.component';
import { RecoveryComponent } from './pages/recovery/recovery.component'
import { Role } from './guards/role';

import { AuthGuard } from './guards/auth.guard';

const routes: Routes = [
  { path: '', component: LandingComponent},
  { path: 'notFound', component: NotFoundPageComponent},
  { path: 'login', component: LoginComponent},
  { path: 'sign-up', component: RegistrationComponent},
  { path: 'password-recovery', component: RecoveryComponent},
  { path: '**', redirectTo: '/notFound'},
  { path: 'admin',
    loadChildren: './pages/admin/admin.module#AdminModule',
    canActivate: [AuthGuard],
    data: {roles: [Role.Admin]}
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
