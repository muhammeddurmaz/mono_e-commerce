import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { OrderFormService, OrderFormGroup } from './order-form.service';
import { IOrder } from '../order.model';
import { OrderService } from '../service/order.service';
import { IUserAdress } from 'app/entities/user-adress/user-adress.model';
import { UserAdressService } from 'app/entities/user-adress/service/user-adress.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { OrderStatus } from 'app/entities/enumerations/order-status.model';

@Component({
  selector: 'jhi-order-update',
  templateUrl: './order-update.component.html',
})
export class OrderUpdateComponent implements OnInit {
  isSaving = false;
  order: IOrder | null = null;
  orderStatusValues = Object.keys(OrderStatus);

  userAdressesSharedCollection: IUserAdress[] = [];
  usersSharedCollection: IUser[] = [];

  editForm: OrderFormGroup = this.orderFormService.createOrderFormGroup();

  constructor(
    protected orderService: OrderService,
    protected orderFormService: OrderFormService,
    protected userAdressService: UserAdressService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUserAdress = (o1: IUserAdress | null, o2: IUserAdress | null): boolean => this.userAdressService.compareUserAdress(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ order }) => {
      this.order = order;
      if (order) {
        this.updateForm(order);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const order = this.orderFormService.getOrder(this.editForm);
    if (order.id !== null) {
      this.subscribeToSaveResponse(this.orderService.update(order));
    } else {
      this.subscribeToSaveResponse(this.orderService.create(order));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrder>>): void {
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

  protected updateForm(order: IOrder): void {
    this.order = order;
    this.orderFormService.resetForm(this.editForm, order);

    this.userAdressesSharedCollection = this.userAdressService.addUserAdressToCollectionIfMissing<IUserAdress>(
      this.userAdressesSharedCollection,
      order.adress
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, order.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userAdressService
      .query()
      .pipe(map((res: HttpResponse<IUserAdress[]>) => res.body ?? []))
      .pipe(
        map((userAdresses: IUserAdress[]) =>
          this.userAdressService.addUserAdressToCollectionIfMissing<IUserAdress>(userAdresses, this.order?.adress)
        )
      )
      .subscribe((userAdresses: IUserAdress[]) => (this.userAdressesSharedCollection = userAdresses));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.order?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
