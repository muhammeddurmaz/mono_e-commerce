import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PropertyDesComponent } from '../list/property-des.component';
import { PropertyDesDetailComponent } from '../detail/property-des-detail.component';
import { PropertyDesUpdateComponent } from '../update/property-des-update.component';
import { PropertyDesRoutingResolveService } from './property-des-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const propertyDesRoute: Routes = [
  {
    path: '',
    component: PropertyDesComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PropertyDesDetailComponent,
    resolve: {
      propertyDes: PropertyDesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PropertyDesUpdateComponent,
    resolve: {
      propertyDes: PropertyDesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PropertyDesUpdateComponent,
    resolve: {
      propertyDes: PropertyDesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(propertyDesRoute)],
  exports: [RouterModule],
})
export class PropertyDesRoutingModule {}
