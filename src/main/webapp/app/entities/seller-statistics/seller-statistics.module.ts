import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SellerStatisticsComponent } from './list/seller-statistics.component';
import { SellerStatisticsDetailComponent } from './detail/seller-statistics-detail.component';
import { SellerStatisticsRoutingModule } from './route/seller-statistics-routing.module';

@NgModule({
  imports: [SharedModule, SellerStatisticsRoutingModule],
  declarations: [SellerStatisticsComponent, SellerStatisticsDetailComponent],
})
export class SellerStatisticsModule {}
