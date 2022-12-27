import { IProductType, NewProductType } from './product-type.model';

export const sampleWithRequiredData: IProductType = {
  id: 29562,
  name: 'Licensed',
};

export const sampleWithPartialData: IProductType = {
  id: 59266,
  name: 'Developer Assistant Steel',
};

export const sampleWithFullData: IProductType = {
  id: 62887,
  name: 'Checking Borders',
};

export const sampleWithNewData: NewProductType = {
  name: 'Nebraska',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
