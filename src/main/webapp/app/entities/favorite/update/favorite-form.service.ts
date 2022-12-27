import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFavorite, NewFavorite } from '../favorite.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFavorite for edit and NewFavoriteFormGroupInput for create.
 */
type FavoriteFormGroupInput = IFavorite | PartialWithRequiredKeyOf<NewFavorite>;

type FavoriteFormDefaults = Pick<NewFavorite, 'id'>;

type FavoriteFormGroupContent = {
  id: FormControl<IFavorite['id'] | NewFavorite['id']>;
  product: FormControl<IFavorite['product']>;
  user: FormControl<IFavorite['user']>;
};

export type FavoriteFormGroup = FormGroup<FavoriteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FavoriteFormService {
  createFavoriteFormGroup(favorite: FavoriteFormGroupInput = { id: null }): FavoriteFormGroup {
    const favoriteRawValue = {
      ...this.getFormDefaults(),
      ...favorite,
    };
    return new FormGroup<FavoriteFormGroupContent>({
      id: new FormControl(
        { value: favoriteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      product: new FormControl(favoriteRawValue.product, {
        validators: [Validators.required],
      }),
      user: new FormControl(favoriteRawValue.user),
    });
  }

  getFavorite(form: FavoriteFormGroup): IFavorite | NewFavorite {
    return form.getRawValue() as IFavorite | NewFavorite;
  }

  resetForm(form: FavoriteFormGroup, favorite: FavoriteFormGroupInput): void {
    const favoriteRawValue = { ...this.getFormDefaults(), ...favorite };
    form.reset(
      {
        ...favoriteRawValue,
        id: { value: favoriteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FavoriteFormDefaults {
    return {
      id: null,
    };
  }
}
