import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { UserCartComponent } from './list/user-cart.component';
import { UserCartDetailComponent } from './detail/user-cart-detail.component';
import { UserCartUpdateComponent } from './update/user-cart-update.component';
import { UserCartDeleteDialogComponent } from './delete/user-cart-delete-dialog.component';
import { UserCartRoutingModule } from './route/user-cart-routing.module';

@NgModule({
  imports: [SharedModule, UserCartRoutingModule],
  declarations: [UserCartComponent, UserCartDetailComponent, UserCartUpdateComponent, UserCartDeleteDialogComponent],
})
export class UserCartModule {}
