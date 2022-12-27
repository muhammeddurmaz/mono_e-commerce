import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { UserAdressComponent } from './list/user-adress.component';
import { UserAdressDetailComponent } from './detail/user-adress-detail.component';
import { UserAdressUpdateComponent } from './update/user-adress-update.component';
import { UserAdressDeleteDialogComponent } from './delete/user-adress-delete-dialog.component';
import { UserAdressRoutingModule } from './route/user-adress-routing.module';

@NgModule({
  imports: [SharedModule, UserAdressRoutingModule],
  declarations: [UserAdressComponent, UserAdressDetailComponent, UserAdressUpdateComponent, UserAdressDeleteDialogComponent],
})
export class UserAdressModule {}
