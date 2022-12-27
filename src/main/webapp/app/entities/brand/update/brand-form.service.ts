import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IBrand, NewBrand } from '../brand.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBrand for edit and NewBrandFormGroupInput for create.
 */
type BrandFormGroupInput = IBrand | PartialWithRequiredKeyOf<NewBrand>;

type BrandFormDefaults = Pick<NewBrand, 'id'>;

type BrandFormGroupContent = {
  id: FormControl<IBrand['id'] | NewBrand['id']>;
  name: FormControl<IBrand['name']>;
  logo: FormControl<IBrand['logo']>;
  logoContentType: FormControl<IBrand['logoContentType']>;
  seller: FormControl<IBrand['seller']>;
};

export type BrandFormGroup = FormGroup<BrandFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BrandFormService {
  createBrandFormGroup(brand: BrandFormGroupInput = { id: null }): BrandFormGroup {
    const brandRawValue = {
      ...this.getFormDefaults(),
      ...brand,
    };
    return new FormGroup<BrandFormGroupContent>({
      id: new FormControl(
        { value: brandRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(brandRawValue.name, {
        validators: [Validators.required, Validators.minLength(3)],
      }),
      logo: new FormControl(brandRawValue.logo, {
        validators: [Validators.required],
      }),
      logoContentType: new FormControl(brandRawValue.logoContentType),
      seller: new FormControl(brandRawValue.seller),
    });
  }

  getBrand(form: BrandFormGroup): IBrand | NewBrand {
    return form.getRawValue() as IBrand | NewBrand;
  }

  resetForm(form: BrandFormGroup, brand: BrandFormGroupInput): void {
    const brandRawValue = { ...this.getFormDefaults(), ...brand };
    form.reset(
      {
        ...brandRawValue,
        id: { value: brandRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): BrandFormDefaults {
    return {
      id: null,
    };
  }
}
