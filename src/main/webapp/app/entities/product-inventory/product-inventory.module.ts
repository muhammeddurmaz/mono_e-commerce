import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ProductInventoryComponent } from './list/product-inventory.component';
import { ProductInventoryDetailComponent } from './detail/product-inventory-detail.component';
import { ProductInventoryUpdateComponent } from './update/product-inventory-update.component';
import { ProductInventoryDeleteDialogComponent } from './delete/product-inventory-delete-dialog.component';
import { ProductInventoryRoutingModule } from './route/product-inventory-routing.module';

@NgModule({
  imports: [SharedModule, ProductInventoryRoutingModule],
  declarations: [
    ProductInventoryComponent,
    ProductInventoryDetailComponent,
    ProductInventoryUpdateComponent,
    ProductInventoryDeleteDialogComponent,
  ],
})
export class ProductInventoryModule {}
