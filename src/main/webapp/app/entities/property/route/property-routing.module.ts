import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PropertyComponent } from '../list/property.component';
import { PropertyDetailComponent } from '../detail/property-detail.component';
import { PropertyUpdateComponent } from '../update/property-update.component';
import { PropertyRoutingResolveService } from './property-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const propertyRoute: Routes = [
  {
    path: '',
    component: PropertyComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PropertyDetailComponent,
    resolve: {
      property: PropertyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PropertyUpdateComponent,
    resolve: {
      property: PropertyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PropertyUpdateComponent,
    resolve: {
      property: PropertyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(propertyRoute)],
  exports: [RouterModule],
})
export class PropertyRoutingModule {}
