import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IUserCart, NewUserCart } from '../user-cart.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUserCart for edit and NewUserCartFormGroupInput for create.
 */
type UserCartFormGroupInput = IUserCart | PartialWithRequiredKeyOf<NewUserCart>;

type UserCartFormDefaults = Pick<NewUserCart, 'id'>;

type UserCartFormGroupContent = {
  id: FormControl<IUserCart['id'] | NewUserCart['id']>;
  cartName: FormControl<IUserCart['cartName']>;
  cartNumber: FormControl<IUserCart['cartNumber']>;
  name: FormControl<IUserCart['name']>;
  lastName: FormControl<IUserCart['lastName']>;
  sktAy: FormControl<IUserCart['sktAy']>;
  sktYil: FormControl<IUserCart['sktYil']>;
  user: FormControl<IUserCart['user']>;
};

export type UserCartFormGroup = FormGroup<UserCartFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UserCartFormService {
  createUserCartFormGroup(userCart: UserCartFormGroupInput = { id: null }): UserCartFormGroup {
    const userCartRawValue = {
      ...this.getFormDefaults(),
      ...userCart,
    };
    return new FormGroup<UserCartFormGroupContent>({
      id: new FormControl(
        { value: userCartRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      cartName: new FormControl(userCartRawValue.cartName, {
        validators: [Validators.required],
      }),
      cartNumber: new FormControl(userCartRawValue.cartNumber, {
        validators: [Validators.required],
      }),
      name: new FormControl(userCartRawValue.name, {
        validators: [Validators.required],
      }),
      lastName: new FormControl(userCartRawValue.lastName, {
        validators: [Validators.required],
      }),
      sktAy: new FormControl(userCartRawValue.sktAy, {
        validators: [Validators.required],
      }),
      sktYil: new FormControl(userCartRawValue.sktYil, {
        validators: [Validators.required],
      }),
      user: new FormControl(userCartRawValue.user),
    });
  }

  getUserCart(form: UserCartFormGroup): IUserCart | NewUserCart {
    return form.getRawValue() as IUserCart | NewUserCart;
  }

  resetForm(form: UserCartFormGroup, userCart: UserCartFormGroupInput): void {
    const userCartRawValue = { ...this.getFormDefaults(), ...userCart };
    form.reset(
      {
        ...userCartRawValue,
        id: { value: userCartRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): UserCartFormDefaults {
    return {
      id: null,
    };
  }
}
