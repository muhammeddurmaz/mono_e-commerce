import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IProductDiscount } from '../product-discount.model';
import { ProductDiscountService } from '../service/product-discount.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './product-discount-delete-dialog.component.html',
})
export class ProductDiscountDeleteDialogComponent {
  productDiscount?: IProductDiscount;

  constructor(protected productDiscountService: ProductDiscountService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.productDiscountService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
