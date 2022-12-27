import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPropertyDes } from '../property-des.model';
import { PropertyDesService } from '../service/property-des.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './property-des-delete-dialog.component.html',
})
export class PropertyDesDeleteDialogComponent {
  propertyDes?: IPropertyDes;

  constructor(protected propertyDesService: PropertyDesService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.propertyDesService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
