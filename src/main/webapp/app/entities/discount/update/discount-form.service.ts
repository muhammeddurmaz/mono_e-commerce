import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IDiscount, NewDiscount } from '../discount.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDiscount for edit and NewDiscountFormGroupInput for create.
 */
type DiscountFormGroupInput = IDiscount | PartialWithRequiredKeyOf<NewDiscount>;

type DiscountFormDefaults = Pick<NewDiscount, 'id'>;

type DiscountFormGroupContent = {
  id: FormControl<IDiscount['id'] | NewDiscount['id']>;
  title: FormControl<IDiscount['title']>;
  image: FormControl<IDiscount['image']>;
  imageContentType: FormControl<IDiscount['imageContentType']>;
  description: FormControl<IDiscount['description']>;
};

export type DiscountFormGroup = FormGroup<DiscountFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DiscountFormService {
  createDiscountFormGroup(discount: DiscountFormGroupInput = { id: null }): DiscountFormGroup {
    const discountRawValue = {
      ...this.getFormDefaults(),
      ...discount,
    };
    return new FormGroup<DiscountFormGroupContent>({
      id: new FormControl(
        { value: discountRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      title: new FormControl(discountRawValue.title, {
        validators: [Validators.required],
      }),
      image: new FormControl(discountRawValue.image),
      imageContentType: new FormControl(discountRawValue.imageContentType),
      description: new FormControl(discountRawValue.description),
    });
  }

  getDiscount(form: DiscountFormGroup): IDiscount | NewDiscount {
    return form.getRawValue() as IDiscount | NewDiscount;
  }

  resetForm(form: DiscountFormGroup, discount: DiscountFormGroupInput): void {
    const discountRawValue = { ...this.getFormDefaults(), ...discount };
    form.reset(
      {
        ...discountRawValue,
        id: { value: discountRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): DiscountFormDefaults {
    return {
      id: null,
    };
  }
}
