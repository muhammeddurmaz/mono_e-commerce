import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UserAdressComponent } from '../list/user-adress.component';
import { UserAdressDetailComponent } from '../detail/user-adress-detail.component';
import { UserAdressUpdateComponent } from '../update/user-adress-update.component';
import { UserAdressRoutingResolveService } from './user-adress-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const userAdressRoute: Routes = [
  {
    path: '',
    component: UserAdressComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UserAdressDetailComponent,
    resolve: {
      userAdress: UserAdressRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UserAdressUpdateComponent,
    resolve: {
      userAdress: UserAdressRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UserAdressUpdateComponent,
    resolve: {
      userAdress: UserAdressRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(userAdressRoute)],
  exports: [RouterModule],
})
export class UserAdressRoutingModule {}
