import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PropertyDesComponent } from './list/property-des.component';
import { PropertyDesDetailComponent } from './detail/property-des-detail.component';
import { PropertyDesUpdateComponent } from './update/property-des-update.component';
import { PropertyDesDeleteDialogComponent } from './delete/property-des-delete-dialog.component';
import { PropertyDesRoutingModule } from './route/property-des-routing.module';

@NgModule({
  imports: [SharedModule, PropertyDesRoutingModule],
  declarations: [PropertyDesComponent, PropertyDesDetailComponent, PropertyDesUpdateComponent, PropertyDesDeleteDialogComponent],
})
export class PropertyDesModule {}
