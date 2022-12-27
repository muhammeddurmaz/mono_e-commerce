import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ProductModelComponent } from './list/product-model.component';
import { ProductModelDetailComponent } from './detail/product-model-detail.component';
import { ProductModelUpdateComponent } from './update/product-model-update.component';
import { ProductModelDeleteDialogComponent } from './delete/product-model-delete-dialog.component';
import { ProductModelRoutingModule } from './route/product-model-routing.module';

@NgModule({
  imports: [SharedModule, ProductModelRoutingModule],
  declarations: [ProductModelComponent, ProductModelDetailComponent, ProductModelUpdateComponent, ProductModelDeleteDialogComponent],
})
export class ProductModelModule {}
