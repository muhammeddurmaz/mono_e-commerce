import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ProductImageComponent } from './list/product-image.component';
import { ProductImageDetailComponent } from './detail/product-image-detail.component';
import { ProductImageUpdateComponent } from './update/product-image-update.component';
import { ProductImageDeleteDialogComponent } from './delete/product-image-delete-dialog.component';
import { ProductImageRoutingModule } from './route/product-image-routing.module';

@NgModule({
  imports: [SharedModule, ProductImageRoutingModule],
  declarations: [ProductImageComponent, ProductImageDetailComponent, ProductImageUpdateComponent, ProductImageDeleteDialogComponent],
})
export class ProductImageModule {}
