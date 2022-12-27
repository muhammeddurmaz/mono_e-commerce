import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPropertyDes, NewPropertyDes } from '../property-des.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPropertyDes for edit and NewPropertyDesFormGroupInput for create.
 */
type PropertyDesFormGroupInput = IPropertyDes | PartialWithRequiredKeyOf<NewPropertyDes>;

type PropertyDesFormDefaults = Pick<NewPropertyDes, 'id'>;

type PropertyDesFormGroupContent = {
  id: FormControl<IPropertyDes['id'] | NewPropertyDes['id']>;
  detail: FormControl<IPropertyDes['detail']>;
  property: FormControl<IPropertyDes['property']>;
  product: FormControl<IPropertyDes['product']>;
};

export type PropertyDesFormGroup = FormGroup<PropertyDesFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PropertyDesFormService {
  createPropertyDesFormGroup(propertyDes: PropertyDesFormGroupInput = { id: null }): PropertyDesFormGroup {
    const propertyDesRawValue = {
      ...this.getFormDefaults(),
      ...propertyDes,
    };
    return new FormGroup<PropertyDesFormGroupContent>({
      id: new FormControl(
        { value: propertyDesRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      detail: new FormControl(propertyDesRawValue.detail, {
        validators: [Validators.required],
      }),
      property: new FormControl(propertyDesRawValue.property, {
        validators: [Validators.required],
      }),
      product: new FormControl(propertyDesRawValue.product, {
        validators: [Validators.required],
      }),
    });
  }

  getPropertyDes(form: PropertyDesFormGroup): IPropertyDes | NewPropertyDes {
    return form.getRawValue() as IPropertyDes | NewPropertyDes;
  }

  resetForm(form: PropertyDesFormGroup, propertyDes: PropertyDesFormGroupInput): void {
    const propertyDesRawValue = { ...this.getFormDefaults(), ...propertyDes };
    form.reset(
      {
        ...propertyDesRawValue,
        id: { value: propertyDesRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PropertyDesFormDefaults {
    return {
      id: null,
    };
  }
}
