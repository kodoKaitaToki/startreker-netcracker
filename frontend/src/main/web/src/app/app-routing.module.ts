import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { LandingComponent } from './pages/landing/landing.component';
import { NotFoundPageComponent } from './pages/not-found-page/not-found-page.component';
import {LoginComponent} from './pages/login/login.component';
import {RegistrationComponent} from './pages/registration/registration.component'

import { AuthGuard } from './guards/auth.guard';

const routes: Routes = [
  { path: '', component: LandingComponent},
  { path: 'notFound', component: NotFoundPageComponent},
  { path: 'login', component: LoginComponent},
  { path: 'sign-up', component: RegistrationComponent},
  { path: '**', redirectTo: '/notFound'},
  { path: 'succes-page', component: RegistrationComponent, canActivate: [AuthGuard] }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
