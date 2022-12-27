import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IProductDiscount, NewProductDiscount } from '../product-discount.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductDiscount for edit and NewProductDiscountFormGroupInput for create.
 */
type ProductDiscountFormGroupInput = IProductDiscount | PartialWithRequiredKeyOf<NewProductDiscount>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IProductDiscount | NewProductDiscount> = Omit<T, 'addedDate' | 'dueDate'> & {
  addedDate?: string | null;
  dueDate?: string | null;
};

type ProductDiscountFormRawValue = FormValueOf<IProductDiscount>;

type NewProductDiscountFormRawValue = FormValueOf<NewProductDiscount>;

type ProductDiscountFormDefaults = Pick<NewProductDiscount, 'id' | 'addedDate' | 'dueDate'>;

type ProductDiscountFormGroupContent = {
  id: FormControl<ProductDiscountFormRawValue['id'] | NewProductDiscount['id']>;
  addedDate: FormControl<ProductDiscountFormRawValue['addedDate']>;
  dueDate: FormControl<ProductDiscountFormRawValue['dueDate']>;
  description: FormControl<ProductDiscountFormRawValue['description']>;
  discount: FormControl<ProductDiscountFormRawValue['discount']>;
  product: FormControl<ProductDiscountFormRawValue['product']>;
};

export type ProductDiscountFormGroup = FormGroup<ProductDiscountFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProductDiscountFormService {
  createProductDiscountFormGroup(productDiscount: ProductDiscountFormGroupInput = { id: null }): ProductDiscountFormGroup {
    const productDiscountRawValue = this.convertProductDiscountToProductDiscountRawValue({
      ...this.getFormDefaults(),
      ...productDiscount,
    });
    return new FormGroup<ProductDiscountFormGroupContent>({
      id: new FormControl(
        { value: productDiscountRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      addedDate: new FormControl(productDiscountRawValue.addedDate, {
        validators: [Validators.required],
      }),
      dueDate: new FormControl(productDiscountRawValue.dueDate, {
        validators: [Validators.required],
      }),
      description: new FormControl(productDiscountRawValue.description),
      discount: new FormControl(productDiscountRawValue.discount, {
        validators: [Validators.required],
      }),
      product: new FormControl(productDiscountRawValue.product, {
        validators: [Validators.required],
      }),
    });
  }

  getProductDiscount(form: ProductDiscountFormGroup): IProductDiscount | NewProductDiscount {
    return this.convertProductDiscountRawValueToProductDiscount(
      form.getRawValue() as ProductDiscountFormRawValue | NewProductDiscountFormRawValue
    );
  }

  resetForm(form: ProductDiscountFormGroup, productDiscount: ProductDiscountFormGroupInput): void {
    const productDiscountRawValue = this.convertProductDiscountToProductDiscountRawValue({ ...this.getFormDefaults(), ...productDiscount });
    form.reset(
      {
        ...productDiscountRawValue,
        id: { value: productDiscountRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ProductDiscountFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      addedDate: currentTime,
      dueDate: currentTime,
    };
  }

  private convertProductDiscountRawValueToProductDiscount(
    rawProductDiscount: ProductDiscountFormRawValue | NewProductDiscountFormRawValue
  ): IProductDiscount | NewProductDiscount {
    return {
      ...rawProductDiscount,
      addedDate: dayjs(rawProductDiscount.addedDate, DATE_TIME_FORMAT),
      dueDate: dayjs(rawProductDiscount.dueDate, DATE_TIME_FORMAT),
    };
  }

  private convertProductDiscountToProductDiscountRawValue(
    productDiscount: IProductDiscount | (Partial<NewProductDiscount> & ProductDiscountFormDefaults)
  ): ProductDiscountFormRawValue | PartialWithRequiredKeyOf<NewProductDiscountFormRawValue> {
    return {
      ...productDiscount,
      addedDate: productDiscount.addedDate ? productDiscount.addedDate.format(DATE_TIME_FORMAT) : undefined,
      dueDate: productDiscount.dueDate ? productDiscount.dueDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
