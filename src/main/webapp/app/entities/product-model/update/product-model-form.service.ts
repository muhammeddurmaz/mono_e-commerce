import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IProductModel, NewProductModel } from '../product-model.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductModel for edit and NewProductModelFormGroupInput for create.
 */
type ProductModelFormGroupInput = IProductModel | PartialWithRequiredKeyOf<NewProductModel>;

type ProductModelFormDefaults = Pick<NewProductModel, 'id'>;

type ProductModelFormGroupContent = {
  id: FormControl<IProductModel['id'] | NewProductModel['id']>;
  modelCode: FormControl<IProductModel['modelCode']>;
};

export type ProductModelFormGroup = FormGroup<ProductModelFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProductModelFormService {
  createProductModelFormGroup(productModel: ProductModelFormGroupInput = { id: null }): ProductModelFormGroup {
    const productModelRawValue = {
      ...this.getFormDefaults(),
      ...productModel,
    };
    return new FormGroup<ProductModelFormGroupContent>({
      id: new FormControl(
        { value: productModelRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      modelCode: new FormControl(productModelRawValue.modelCode),
    });
  }

  getProductModel(form: ProductModelFormGroup): IProductModel | NewProductModel {
    return form.getRawValue() as IProductModel | NewProductModel;
  }

  resetForm(form: ProductModelFormGroup, productModel: ProductModelFormGroupInput): void {
    const productModelRawValue = { ...this.getFormDefaults(), ...productModel };
    form.reset(
      {
        ...productModelRawValue,
        id: { value: productModelRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ProductModelFormDefaults {
    return {
      id: null,
    };
  }
}
