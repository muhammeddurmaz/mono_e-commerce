import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UserCartComponent } from '../list/user-cart.component';
import { UserCartDetailComponent } from '../detail/user-cart-detail.component';
import { UserCartUpdateComponent } from '../update/user-cart-update.component';
import { UserCartRoutingResolveService } from './user-cart-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const userCartRoute: Routes = [
  {
    path: '',
    component: UserCartComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UserCartDetailComponent,
    resolve: {
      userCart: UserCartRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UserCartUpdateComponent,
    resolve: {
      userCart: UserCartRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UserCartUpdateComponent,
    resolve: {
      userCart: UserCartRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(userCartRoute)],
  exports: [RouterModule],
})
export class UserCartRoutingModule {}
