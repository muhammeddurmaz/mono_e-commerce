import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IUserCart } from '../user-cart.model';
import { UserCartService } from '../service/user-cart.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './user-cart-delete-dialog.component.html',
})
export class UserCartDeleteDialogComponent {
  userCart?: IUserCart;

  constructor(protected userCartService: UserCartService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.userCartService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
