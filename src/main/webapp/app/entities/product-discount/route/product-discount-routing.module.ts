import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProductDiscountComponent } from '../list/product-discount.component';
import { ProductDiscountDetailComponent } from '../detail/product-discount-detail.component';
import { ProductDiscountUpdateComponent } from '../update/product-discount-update.component';
import { ProductDiscountRoutingResolveService } from './product-discount-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const productDiscountRoute: Routes = [
  {
    path: '',
    component: ProductDiscountComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProductDiscountDetailComponent,
    resolve: {
      productDiscount: ProductDiscountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProductDiscountUpdateComponent,
    resolve: {
      productDiscount: ProductDiscountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProductDiscountUpdateComponent,
    resolve: {
      productDiscount: ProductDiscountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(productDiscountRoute)],
  exports: [RouterModule],
})
export class ProductDiscountRoutingModule {}
