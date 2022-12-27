import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ProductStatisticsComponent } from './list/product-statistics.component';
import { ProductStatisticsDetailComponent } from './detail/product-statistics-detail.component';
import { ProductStatisticsRoutingModule } from './route/product-statistics-routing.module';

@NgModule({
  imports: [SharedModule, ProductStatisticsRoutingModule],
  declarations: [ProductStatisticsComponent, ProductStatisticsDetailComponent],
})
export class ProductStatisticsModule {}
