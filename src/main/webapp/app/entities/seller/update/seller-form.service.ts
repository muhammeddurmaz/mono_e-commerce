import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISeller, NewSeller } from '../seller.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISeller for edit and NewSellerFormGroupInput for create.
 */
type SellerFormGroupInput = ISeller | PartialWithRequiredKeyOf<NewSeller>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISeller | NewSeller> = Omit<T, 'placedDate'> & {
  placedDate?: string | null;
};

type SellerFormRawValue = FormValueOf<ISeller>;

type NewSellerFormRawValue = FormValueOf<NewSeller>;

type SellerFormDefaults = Pick<NewSeller, 'id' | 'activated' | 'placedDate'>;

type SellerFormGroupContent = {
  id: FormControl<SellerFormRawValue['id'] | NewSeller['id']>;
  name: FormControl<SellerFormRawValue['name']>;
  lastName: FormControl<SellerFormRawValue['lastName']>;
  shopName: FormControl<SellerFormRawValue['shopName']>;
  mail: FormControl<SellerFormRawValue['mail']>;
  activated: FormControl<SellerFormRawValue['activated']>;
  image: FormControl<SellerFormRawValue['image']>;
  imageContentType: FormControl<SellerFormRawValue['imageContentType']>;
  tckn: FormControl<SellerFormRawValue['tckn']>;
  phone: FormControl<SellerFormRawValue['phone']>;
  city: FormControl<SellerFormRawValue['city']>;
  placedDate: FormControl<SellerFormRawValue['placedDate']>;
  user: FormControl<SellerFormRawValue['user']>;
  sellerProductType: FormControl<SellerFormRawValue['sellerProductType']>;
};

export type SellerFormGroup = FormGroup<SellerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SellerFormService {
  createSellerFormGroup(seller: SellerFormGroupInput = { id: null }): SellerFormGroup {
    const sellerRawValue = this.convertSellerToSellerRawValue({
      ...this.getFormDefaults(),
      ...seller,
    });
    return new FormGroup<SellerFormGroupContent>({
      id: new FormControl(
        { value: sellerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(sellerRawValue.name, {
        validators: [Validators.required, Validators.minLength(3)],
      }),
      lastName: new FormControl(sellerRawValue.lastName, {
        validators: [Validators.required, Validators.minLength(3)],
      }),
      shopName: new FormControl(sellerRawValue.shopName, {
        validators: [Validators.required, Validators.minLength(3)],
      }),
      mail: new FormControl(sellerRawValue.mail, {
        validators: [Validators.required],
      }),
      activated: new FormControl(sellerRawValue.activated),
      image: new FormControl(sellerRawValue.image),
      imageContentType: new FormControl(sellerRawValue.imageContentType),
      tckn: new FormControl(sellerRawValue.tckn, {
        validators: [Validators.required, Validators.minLength(11)],
      }),
      phone: new FormControl(sellerRawValue.phone, {
        validators: [Validators.required],
      }),
      city: new FormControl(sellerRawValue.city, {
        validators: [Validators.required],
      }),
      placedDate: new FormControl(sellerRawValue.placedDate),
      user: new FormControl(sellerRawValue.user),
      sellerProductType: new FormControl(sellerRawValue.sellerProductType),
    });
  }

  getSeller(form: SellerFormGroup): ISeller | NewSeller {
    return this.convertSellerRawValueToSeller(form.getRawValue() as SellerFormRawValue | NewSellerFormRawValue);
  }

  resetForm(form: SellerFormGroup, seller: SellerFormGroupInput): void {
    const sellerRawValue = this.convertSellerToSellerRawValue({ ...this.getFormDefaults(), ...seller });
    form.reset(
      {
        ...sellerRawValue,
        id: { value: sellerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SellerFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      activated: false,
      placedDate: currentTime,
    };
  }

  private convertSellerRawValueToSeller(rawSeller: SellerFormRawValue | NewSellerFormRawValue): ISeller | NewSeller {
    return {
      ...rawSeller,
      placedDate: dayjs(rawSeller.placedDate, DATE_TIME_FORMAT),
    };
  }

  private convertSellerToSellerRawValue(
    seller: ISeller | (Partial<NewSeller> & SellerFormDefaults)
  ): SellerFormRawValue | PartialWithRequiredKeyOf<NewSellerFormRawValue> {
    return {
      ...seller,
      placedDate: seller.placedDate ? seller.placedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
