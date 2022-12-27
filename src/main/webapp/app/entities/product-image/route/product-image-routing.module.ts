import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProductImageComponent } from '../list/product-image.component';
import { ProductImageDetailComponent } from '../detail/product-image-detail.component';
import { ProductImageUpdateComponent } from '../update/product-image-update.component';
import { ProductImageRoutingResolveService } from './product-image-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const productImageRoute: Routes = [
  {
    path: '',
    component: ProductImageComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProductImageDetailComponent,
    resolve: {
      productImage: ProductImageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProductImageUpdateComponent,
    resolve: {
      productImage: ProductImageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProductImageUpdateComponent,
    resolve: {
      productImage: ProductImageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(productImageRoute)],
  exports: [RouterModule],
})
export class ProductImageRoutingModule {}
