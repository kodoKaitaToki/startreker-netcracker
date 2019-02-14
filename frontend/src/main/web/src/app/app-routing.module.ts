import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AdminComponent }      from './admin/admin.component';
import { ApproverCrudComponent } from './admin/approver-crud/approver-crud.component';

const routes: Routes = [
                        { path: 'admin', component: AdminComponent,
                          children: [
                                { path: 'approvers', component: ApproverCrudComponent }
                                ]
                        }
                       ];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
