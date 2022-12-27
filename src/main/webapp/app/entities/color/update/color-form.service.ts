import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IColor, NewColor } from '../color.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IColor for edit and NewColorFormGroupInput for create.
 */
type ColorFormGroupInput = IColor | PartialWithRequiredKeyOf<NewColor>;

type ColorFormDefaults = Pick<NewColor, 'id'>;

type ColorFormGroupContent = {
  id: FormControl<IColor['id'] | NewColor['id']>;
  name: FormControl<IColor['name']>;
  code: FormControl<IColor['code']>;
};

export type ColorFormGroup = FormGroup<ColorFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ColorFormService {
  createColorFormGroup(color: ColorFormGroupInput = { id: null }): ColorFormGroup {
    const colorRawValue = {
      ...this.getFormDefaults(),
      ...color,
    };
    return new FormGroup<ColorFormGroupContent>({
      id: new FormControl(
        { value: colorRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(colorRawValue.name, {
        validators: [Validators.required],
      }),
      code: new FormControl(colorRawValue.code),
    });
  }

  getColor(form: ColorFormGroup): IColor | NewColor {
    return form.getRawValue() as IColor | NewColor;
  }

  resetForm(form: ColorFormGroup, color: ColorFormGroupInput): void {
    const colorRawValue = { ...this.getFormDefaults(), ...color };
    form.reset(
      {
        ...colorRawValue,
        id: { value: colorRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ColorFormDefaults {
    return {
      id: null,
    };
  }
}
