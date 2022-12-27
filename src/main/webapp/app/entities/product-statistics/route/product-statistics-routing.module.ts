import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProductStatisticsComponent } from '../list/product-statistics.component';
import { ProductStatisticsDetailComponent } from '../detail/product-statistics-detail.component';
import { ProductStatisticsRoutingResolveService } from './product-statistics-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const productStatisticsRoute: Routes = [
  {
    path: '',
    component: ProductStatisticsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProductStatisticsDetailComponent,
    resolve: {
      productStatistics: ProductStatisticsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(productStatisticsRoute)],
  exports: [RouterModule],
})
export class ProductStatisticsRoutingModule {}
