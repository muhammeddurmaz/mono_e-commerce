import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PropertyComponent } from './list/property.component';
import { PropertyDetailComponent } from './detail/property-detail.component';
import { PropertyUpdateComponent } from './update/property-update.component';
import { PropertyDeleteDialogComponent } from './delete/property-delete-dialog.component';
import { PropertyRoutingModule } from './route/property-routing.module';

@NgModule({
  imports: [SharedModule, PropertyRoutingModule],
  declarations: [PropertyComponent, PropertyDetailComponent, PropertyUpdateComponent, PropertyDeleteDialogComponent],
})
export class PropertyModule {}
