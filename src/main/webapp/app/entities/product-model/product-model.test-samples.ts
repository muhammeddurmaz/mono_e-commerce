import { IProductModel, NewProductModel } from './product-model.model';

export const sampleWithRequiredData: IProductModel = {
  id: 82578,
};

export const sampleWithPartialData: IProductModel = {
  id: 17719,
  modelCode: 'teal Avon Table',
};

export const sampleWithFullData: IProductModel = {
  id: 51059,
  modelCode: 'Incredible',
};

export const sampleWithNewData: NewProductModel = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
