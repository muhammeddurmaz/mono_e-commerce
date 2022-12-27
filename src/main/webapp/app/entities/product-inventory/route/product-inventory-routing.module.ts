import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProductInventoryComponent } from '../list/product-inventory.component';
import { ProductInventoryDetailComponent } from '../detail/product-inventory-detail.component';
import { ProductInventoryUpdateComponent } from '../update/product-inventory-update.component';
import { ProductInventoryRoutingResolveService } from './product-inventory-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const productInventoryRoute: Routes = [
  {
    path: '',
    component: ProductInventoryComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProductInventoryDetailComponent,
    resolve: {
      productInventory: ProductInventoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProductInventoryUpdateComponent,
    resolve: {
      productInventory: ProductInventoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProductInventoryUpdateComponent,
    resolve: {
      productInventory: ProductInventoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(productInventoryRoute)],
  exports: [RouterModule],
})
export class ProductInventoryRoutingModule {}
