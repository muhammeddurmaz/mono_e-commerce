import { IDiscount, NewDiscount } from './discount.model';

export const sampleWithRequiredData: IDiscount = {
  id: 61250,
  title: 'iterate e-commerce',
};

export const sampleWithPartialData: IDiscount = {
  id: 45487,
  title: 'Island Florida',
};

export const sampleWithFullData: IDiscount = {
  id: 51274,
  title: 'indigo Mouse Handmade',
  image: '../fake-data/blob/hipster.png',
  imageContentType: 'unknown',
  description: 'Gorgeous Bypass',
};

export const sampleWithNewData: NewDiscount = {
  title: 'policy Manager',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
