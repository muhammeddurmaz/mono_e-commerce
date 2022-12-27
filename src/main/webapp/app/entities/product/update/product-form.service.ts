import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IProduct, NewProduct } from '../product.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProduct for edit and NewProductFormGroupInput for create.
 */
type ProductFormGroupInput = IProduct | PartialWithRequiredKeyOf<NewProduct>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IProduct | NewProduct> = Omit<T, 'addedDate'> & {
  addedDate?: string | null;
};

type ProductFormRawValue = FormValueOf<IProduct>;

type NewProductFormRawValue = FormValueOf<NewProduct>;

type ProductFormDefaults = Pick<NewProduct, 'id' | 'addedDate' | 'active'>;

type ProductFormGroupContent = {
  id: FormControl<ProductFormRawValue['id'] | NewProduct['id']>;
  barcode: FormControl<ProductFormRawValue['barcode']>;
  modelCode: FormControl<ProductFormRawValue['modelCode']>;
  name: FormControl<ProductFormRawValue['name']>;
  price: FormControl<ProductFormRawValue['price']>;
  discountPrice: FormControl<ProductFormRawValue['discountPrice']>;
  description: FormControl<ProductFormRawValue['description']>;
  image: FormControl<ProductFormRawValue['image']>;
  imageContentType: FormControl<ProductFormRawValue['imageContentType']>;
  addedDate: FormControl<ProductFormRawValue['addedDate']>;
  rating: FormControl<ProductFormRawValue['rating']>;
  sizee: FormControl<ProductFormRawValue['sizee']>;
  stock: FormControl<ProductFormRawValue['stock']>;
  active: FormControl<ProductFormRawValue['active']>;
  category: FormControl<ProductFormRawValue['category']>;
  subCategory: FormControl<ProductFormRawValue['subCategory']>;
  color: FormControl<ProductFormRawValue['color']>;
  productModel: FormControl<ProductFormRawValue['productModel']>;
  seller: FormControl<ProductFormRawValue['seller']>;
  brand: FormControl<ProductFormRawValue['brand']>;
};

export type ProductFormGroup = FormGroup<ProductFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProductFormService {
  createProductFormGroup(product: ProductFormGroupInput = { id: null }): ProductFormGroup {
    const productRawValue = this.convertProductToProductRawValue({
      ...this.getFormDefaults(),
      ...product,
    });
    return new FormGroup<ProductFormGroupContent>({
      id: new FormControl(
        { value: productRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      barcode: new FormControl(productRawValue.barcode, {
        validators: [Validators.required],
      }),
      modelCode: new FormControl(productRawValue.modelCode, {
        validators: [Validators.required],
      }),
      name: new FormControl(productRawValue.name, {
        validators: [Validators.required, Validators.minLength(5)],
      }),
      price: new FormControl(productRawValue.price, {
        validators: [Validators.required, Validators.min(0)],
      }),
      discountPrice: new FormControl(productRawValue.discountPrice),
      description: new FormControl(productRawValue.description),
      image: new FormControl(productRawValue.image, {
        validators: [Validators.required],
      }),
      imageContentType: new FormControl(productRawValue.imageContentType),
      addedDate: new FormControl(productRawValue.addedDate, {
        validators: [Validators.required],
      }),
      rating: new FormControl(productRawValue.rating),
      sizee: new FormControl(productRawValue.sizee),
      stock: new FormControl(productRawValue.stock, {
        validators: [Validators.required, Validators.min(0)],
      }),
      active: new FormControl(productRawValue.active),
      category: new FormControl(productRawValue.category, {
        validators: [Validators.required],
      }),
      subCategory: new FormControl(productRawValue.subCategory),
      color: new FormControl(productRawValue.color),
      productModel: new FormControl(productRawValue.productModel),
      seller: new FormControl(productRawValue.seller),
      brand: new FormControl(productRawValue.brand),
    });
  }

  getProduct(form: ProductFormGroup): IProduct | NewProduct {
    return this.convertProductRawValueToProduct(form.getRawValue() as ProductFormRawValue | NewProductFormRawValue);
  }

  resetForm(form: ProductFormGroup, product: ProductFormGroupInput): void {
    const productRawValue = this.convertProductToProductRawValue({ ...this.getFormDefaults(), ...product });
    form.reset(
      {
        ...productRawValue,
        id: { value: productRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ProductFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      addedDate: currentTime,
      active: false,
    };
  }

  private convertProductRawValueToProduct(rawProduct: ProductFormRawValue | NewProductFormRawValue): IProduct | NewProduct {
    return {
      ...rawProduct,
      addedDate: dayjs(rawProduct.addedDate, DATE_TIME_FORMAT),
    };
  }

  private convertProductToProductRawValue(
    product: IProduct | (Partial<NewProduct> & ProductFormDefaults)
  ): ProductFormRawValue | PartialWithRequiredKeyOf<NewProductFormRawValue> {
    return {
      ...product,
      addedDate: product.addedDate ? product.addedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
