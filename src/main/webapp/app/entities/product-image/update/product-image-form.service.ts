import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IProductImage, NewProductImage } from '../product-image.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductImage for edit and NewProductImageFormGroupInput for create.
 */
type ProductImageFormGroupInput = IProductImage | PartialWithRequiredKeyOf<NewProductImage>;

type ProductImageFormDefaults = Pick<NewProductImage, 'id'>;

type ProductImageFormGroupContent = {
  id: FormControl<IProductImage['id'] | NewProductImage['id']>;
  image1: FormControl<IProductImage['image1']>;
  image1ContentType: FormControl<IProductImage['image1ContentType']>;
  image2: FormControl<IProductImage['image2']>;
  image2ContentType: FormControl<IProductImage['image2ContentType']>;
  image3: FormControl<IProductImage['image3']>;
  image3ContentType: FormControl<IProductImage['image3ContentType']>;
  image4: FormControl<IProductImage['image4']>;
  image4ContentType: FormControl<IProductImage['image4ContentType']>;
  image5: FormControl<IProductImage['image5']>;
  image5ContentType: FormControl<IProductImage['image5ContentType']>;
  image6: FormControl<IProductImage['image6']>;
  image6ContentType: FormControl<IProductImage['image6ContentType']>;
  product: FormControl<IProductImage['product']>;
};

export type ProductImageFormGroup = FormGroup<ProductImageFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProductImageFormService {
  createProductImageFormGroup(productImage: ProductImageFormGroupInput = { id: null }): ProductImageFormGroup {
    const productImageRawValue = {
      ...this.getFormDefaults(),
      ...productImage,
    };
    return new FormGroup<ProductImageFormGroupContent>({
      id: new FormControl(
        { value: productImageRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      image1: new FormControl(productImageRawValue.image1, {
        validators: [Validators.required],
      }),
      image1ContentType: new FormControl(productImageRawValue.image1ContentType),
      image2: new FormControl(productImageRawValue.image2),
      image2ContentType: new FormControl(productImageRawValue.image2ContentType),
      image3: new FormControl(productImageRawValue.image3),
      image3ContentType: new FormControl(productImageRawValue.image3ContentType),
      image4: new FormControl(productImageRawValue.image4),
      image4ContentType: new FormControl(productImageRawValue.image4ContentType),
      image5: new FormControl(productImageRawValue.image5),
      image5ContentType: new FormControl(productImageRawValue.image5ContentType),
      image6: new FormControl(productImageRawValue.image6),
      image6ContentType: new FormControl(productImageRawValue.image6ContentType),
      product: new FormControl(productImageRawValue.product, {
        validators: [Validators.required],
      }),
    });
  }

  getProductImage(form: ProductImageFormGroup): IProductImage | NewProductImage {
    return form.getRawValue() as IProductImage | NewProductImage;
  }

  resetForm(form: ProductImageFormGroup, productImage: ProductImageFormGroupInput): void {
    const productImageRawValue = { ...this.getFormDefaults(), ...productImage };
    form.reset(
      {
        ...productImageRawValue,
        id: { value: productImageRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ProductImageFormDefaults {
    return {
      id: null,
    };
  }
}
