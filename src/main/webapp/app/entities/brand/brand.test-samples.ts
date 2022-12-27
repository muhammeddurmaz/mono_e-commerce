import { IBrand, NewBrand } from './brand.model';

export const sampleWithRequiredData: IBrand = {
  id: 69581,
  name: 'Human copying experiences',
  logo: '../fake-data/blob/hipster.png',
  logoContentType: 'unknown',
};

export const sampleWithPartialData: IBrand = {
  id: 68630,
  name: 'mobile Pizza',
  logo: '../fake-data/blob/hipster.png',
  logoContentType: 'unknown',
};

export const sampleWithFullData: IBrand = {
  id: 53595,
  name: 'Metal Sol empower',
  logo: '../fake-data/blob/hipster.png',
  logoContentType: 'unknown',
};

export const sampleWithNewData: NewBrand = {
  name: 'primary',
  logo: '../fake-data/blob/hipster.png',
  logoContentType: 'unknown',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
