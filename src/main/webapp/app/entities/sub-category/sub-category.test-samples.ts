import { ISubCategory, NewSubCategory } from './sub-category.model';

export const sampleWithRequiredData: ISubCategory = {
  id: 68785,
  name: 'payment',
  image: '../fake-data/blob/hipster.png',
  imageContentType: 'unknown',
};

export const sampleWithPartialData: ISubCategory = {
  id: 90188,
  name: 'global matrix',
  image: '../fake-data/blob/hipster.png',
  imageContentType: 'unknown',
};

export const sampleWithFullData: ISubCategory = {
  id: 493,
  name: 'THX',
  image: '../fake-data/blob/hipster.png',
  imageContentType: 'unknown',
};

export const sampleWithNewData: NewSubCategory = {
  name: 'Rubber',
  image: '../fake-data/blob/hipster.png',
  imageContentType: 'unknown',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
