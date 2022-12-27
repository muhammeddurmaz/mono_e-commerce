import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ProductDiscountComponent } from './list/product-discount.component';
import { ProductDiscountDetailComponent } from './detail/product-discount-detail.component';
import { ProductDiscountUpdateComponent } from './update/product-discount-update.component';
import { ProductDiscountDeleteDialogComponent } from './delete/product-discount-delete-dialog.component';
import { ProductDiscountRoutingModule } from './route/product-discount-routing.module';

@NgModule({
  imports: [SharedModule, ProductDiscountRoutingModule],
  declarations: [
    ProductDiscountComponent,
    ProductDiscountDetailComponent,
    ProductDiscountUpdateComponent,
    ProductDiscountDeleteDialogComponent,
  ],
})
export class ProductDiscountModule {}
