import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PaymentFormService, PaymentFormGroup } from './payment-form.service';
import { IPayment } from '../payment.model';
import { PaymentService } from '../service/payment.service';
import { IOrder } from 'app/entities/order/order.model';
import { OrderService } from 'app/entities/order/service/order.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IUserCart } from 'app/entities/user-cart/user-cart.model';
import { UserCartService } from 'app/entities/user-cart/service/user-cart.service';

@Component({
  selector: 'jhi-payment-update',
  templateUrl: './payment-update.component.html',
})
export class PaymentUpdateComponent implements OnInit {
  isSaving = false;
  payment: IPayment | null = null;

  ordersCollection: IOrder[] = [];
  usersSharedCollection: IUser[] = [];
  userCartsSharedCollection: IUserCart[] = [];

  editForm: PaymentFormGroup = this.paymentFormService.createPaymentFormGroup();

  constructor(
    protected paymentService: PaymentService,
    protected paymentFormService: PaymentFormService,
    protected orderService: OrderService,
    protected userService: UserService,
    protected userCartService: UserCartService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareOrder = (o1: IOrder | null, o2: IOrder | null): boolean => this.orderService.compareOrder(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareUserCart = (o1: IUserCart | null, o2: IUserCart | null): boolean => this.userCartService.compareUserCart(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ payment }) => {
      this.payment = payment;
      if (payment) {
        this.updateForm(payment);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const payment = this.paymentFormService.getPayment(this.editForm);
    if (payment.id !== null) {
      this.subscribeToSaveResponse(this.paymentService.update(payment));
    } else {
      this.subscribeToSaveResponse(this.paymentService.create(payment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPayment>>): void {
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

  protected updateForm(payment: IPayment): void {
    this.payment = payment;
    this.paymentFormService.resetForm(this.editForm, payment);

    this.ordersCollection = this.orderService.addOrderToCollectionIfMissing<IOrder>(this.ordersCollection, payment.order);
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, payment.user);
    this.userCartsSharedCollection = this.userCartService.addUserCartToCollectionIfMissing<IUserCart>(
      this.userCartsSharedCollection,
      payment.usercart
    );
  }

  protected loadRelationshipsOptions(): void {
    this.orderService
      .query({ filter: 'payment-is-null' })
      .pipe(map((res: HttpResponse<IOrder[]>) => res.body ?? []))
      .pipe(map((orders: IOrder[]) => this.orderService.addOrderToCollectionIfMissing<IOrder>(orders, this.payment?.order)))
      .subscribe((orders: IOrder[]) => (this.ordersCollection = orders));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.payment?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.userCartService
      .query()
      .pipe(map((res: HttpResponse<IUserCart[]>) => res.body ?? []))
      .pipe(
        map((userCarts: IUserCart[]) => this.userCartService.addUserCartToCollectionIfMissing<IUserCart>(userCarts, this.payment?.usercart))
      )
      .subscribe((userCarts: IUserCart[]) => (this.userCartsSharedCollection = userCarts));
  }
}
