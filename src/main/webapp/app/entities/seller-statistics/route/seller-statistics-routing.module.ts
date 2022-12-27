import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SellerStatisticsComponent } from '../list/seller-statistics.component';
import { SellerStatisticsDetailComponent } from '../detail/seller-statistics-detail.component';
import { SellerStatisticsRoutingResolveService } from './seller-statistics-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const sellerStatisticsRoute: Routes = [
  {
    path: '',
    component: SellerStatisticsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SellerStatisticsDetailComponent,
    resolve: {
      sellerStatistics: SellerStatisticsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sellerStatisticsRoute)],
  exports: [RouterModule],
})
export class SellerStatisticsRoutingModule {}
