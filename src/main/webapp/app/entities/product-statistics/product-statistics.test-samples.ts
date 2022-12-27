import { IProductStatistics, NewProductStatistics } from './product-statistics.model';

export const sampleWithRequiredData: IProductStatistics = {
  id: 94954,
};

export const sampleWithPartialData: IProductStatistics = {
  id: 89547,
  comment: 40515,
};

export const sampleWithFullData: IProductStatistics = {
  id: 19912,
  order: 43063,
  click: 62786,
  comment: 88887,
  rating: 11223,
  addCart: 41550,
};

export const sampleWithNewData: NewProductStatistics = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
