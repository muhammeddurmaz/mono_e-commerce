import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProductModelComponent } from '../list/product-model.component';
import { ProductModelDetailComponent } from '../detail/product-model-detail.component';
import { ProductModelUpdateComponent } from '../update/product-model-update.component';
import { ProductModelRoutingResolveService } from './product-model-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const productModelRoute: Routes = [
  {
    path: '',
    component: ProductModelComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProductModelDetailComponent,
    resolve: {
      productModel: ProductModelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProductModelUpdateComponent,
    resolve: {
      productModel: ProductModelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProductModelUpdateComponent,
    resolve: {
      productModel: ProductModelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(productModelRoute)],
  exports: [RouterModule],
})
export class ProductModelRoutingModule {}
