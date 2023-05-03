import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ComputerComponent } from '../list/computer.component';
import { ComputerDetailComponent } from '../detail/computer-detail.component';
import { ComputerUpdateComponent } from '../update/computer-update.component';
import { ComputerRoutingResolveService } from './computer-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const computerRoute: Routes = [
  {
    path: '',
    component: ComputerComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ComputerDetailComponent,
    resolve: {
      computer: ComputerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ComputerUpdateComponent,
    resolve: {
      computer: ComputerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ComputerUpdateComponent,
    resolve: {
      computer: ComputerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(computerRoute)],
  exports: [RouterModule],
})
export class ComputerRoutingModule {}
