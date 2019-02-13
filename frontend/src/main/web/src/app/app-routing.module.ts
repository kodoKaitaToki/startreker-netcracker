import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { NavbarComponent }      from './navbar/navbar.component';
import { RegisterComponent }      from './register/register.component';
import { RecoveryComponent }      from './recovery/recovery.component';
import { LoginComponent }      from './login/login.component';

const routes: Routes = [
 { path: '', component: NavbarComponent },
 { path: 'register', component: RegisterComponent },
 { path: 'recovery', component: RecoveryComponent },
 { path: 'login', component: LoginComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
