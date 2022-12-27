import { IFavorite, NewFavorite } from './favorite.model';

export const sampleWithRequiredData: IFavorite = {
  id: 84905,
};

export const sampleWithPartialData: IFavorite = {
  id: 2360,
};

export const sampleWithFullData: IFavorite = {
  id: 96517,
};

export const sampleWithNewData: NewFavorite = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
