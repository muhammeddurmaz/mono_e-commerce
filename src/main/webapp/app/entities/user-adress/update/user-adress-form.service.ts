import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IUserAdress, NewUserAdress } from '../user-adress.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUserAdress for edit and NewUserAdressFormGroupInput for create.
 */
type UserAdressFormGroupInput = IUserAdress | PartialWithRequiredKeyOf<NewUserAdress>;

type UserAdressFormDefaults = Pick<NewUserAdress, 'id'>;

type UserAdressFormGroupContent = {
  id: FormControl<IUserAdress['id'] | NewUserAdress['id']>;
  name: FormControl<IUserAdress['name']>;
  lastName: FormControl<IUserAdress['lastName']>;
  telephone: FormControl<IUserAdress['telephone']>;
  city: FormControl<IUserAdress['city']>;
  adress: FormControl<IUserAdress['adress']>;
  adressTitle: FormControl<IUserAdress['adressTitle']>;
  user: FormControl<IUserAdress['user']>;
};

export type UserAdressFormGroup = FormGroup<UserAdressFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UserAdressFormService {
  createUserAdressFormGroup(userAdress: UserAdressFormGroupInput = { id: null }): UserAdressFormGroup {
    const userAdressRawValue = {
      ...this.getFormDefaults(),
      ...userAdress,
    };
    return new FormGroup<UserAdressFormGroupContent>({
      id: new FormControl(
        { value: userAdressRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(userAdressRawValue.name, {
        validators: [Validators.required],
      }),
      lastName: new FormControl(userAdressRawValue.lastName, {
        validators: [Validators.required],
      }),
      telephone: new FormControl(userAdressRawValue.telephone, {
        validators: [Validators.required],
      }),
      city: new FormControl(userAdressRawValue.city, {
        validators: [Validators.required],
      }),
      adress: new FormControl(userAdressRawValue.adress),
      adressTitle: new FormControl(userAdressRawValue.adressTitle, {
        validators: [Validators.required],
      }),
      user: new FormControl(userAdressRawValue.user),
    });
  }

  getUserAdress(form: UserAdressFormGroup): IUserAdress | NewUserAdress {
    return form.getRawValue() as IUserAdress | NewUserAdress;
  }

  resetForm(form: UserAdressFormGroup, userAdress: UserAdressFormGroupInput): void {
    const userAdressRawValue = { ...this.getFormDefaults(), ...userAdress };
    form.reset(
      {
        ...userAdressRawValue,
        id: { value: userAdressRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): UserAdressFormDefaults {
    return {
      id: null,
    };
  }
}
