import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IProperty, NewProperty } from '../property.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProperty for edit and NewPropertyFormGroupInput for create.
 */
type PropertyFormGroupInput = IProperty | PartialWithRequiredKeyOf<NewProperty>;

type PropertyFormDefaults = Pick<NewProperty, 'id'>;

type PropertyFormGroupContent = {
  id: FormControl<IProperty['id'] | NewProperty['id']>;
  name: FormControl<IProperty['name']>;
  category: FormControl<IProperty['category']>;
};

export type PropertyFormGroup = FormGroup<PropertyFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PropertyFormService {
  createPropertyFormGroup(property: PropertyFormGroupInput = { id: null }): PropertyFormGroup {
    const propertyRawValue = {
      ...this.getFormDefaults(),
      ...property,
    };
    return new FormGroup<PropertyFormGroupContent>({
      id: new FormControl(
        { value: propertyRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(propertyRawValue.name, {
        validators: [Validators.required],
      }),
      category: new FormControl(propertyRawValue.category),
    });
  }

  getProperty(form: PropertyFormGroup): IProperty | NewProperty {
    return form.getRawValue() as IProperty | NewProperty;
  }

  resetForm(form: PropertyFormGroup, property: PropertyFormGroupInput): void {
    const propertyRawValue = { ...this.getFormDefaults(), ...property };
    form.reset(
      {
        ...propertyRawValue,
        id: { value: propertyRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PropertyFormDefaults {
    return {
      id: null,
    };
  }
}
