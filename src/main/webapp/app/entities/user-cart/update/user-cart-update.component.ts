import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { UserCartFormService, UserCartFormGroup } from './user-cart-form.service';
import { IUserCart } from '../user-cart.model';
import { UserCartService } from '../service/user-cart.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-user-cart-update',
  templateUrl: './user-cart-update.component.html',
})
export class UserCartUpdateComponent implements OnInit {
  isSaving = false;
  userCart: IUserCart | null = null;

  usersSharedCollection: IUser[] = [];

  editForm: UserCartFormGroup = this.userCartFormService.createUserCartFormGroup();

  constructor(
    protected userCartService: UserCartService,
    protected userCartFormService: UserCartFormService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userCart }) => {
      this.userCart = userCart;
      if (userCart) {
        this.updateForm(userCart);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userCart = this.userCartFormService.getUserCart(this.editForm);
    if (userCart.id !== null) {
      this.subscribeToSaveResponse(this.userCartService.update(userCart));
    } else {
      this.subscribeToSaveResponse(this.userCartService.create(userCart));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserCart>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(userCart: IUserCart): void {
    this.userCart = userCart;
    this.userCartFormService.resetForm(this.editForm, userCart);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, userCart.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.userCart?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
