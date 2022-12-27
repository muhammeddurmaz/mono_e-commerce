import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IUserAdress } from '../user-adress.model';
import { UserAdressService } from '../service/user-adress.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './user-adress-delete-dialog.component.html',
})
export class UserAdressDeleteDialogComponent {
  userAdress?: IUserAdress;

  constructor(protected userAdressService: UserAdressService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.userAdressService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
