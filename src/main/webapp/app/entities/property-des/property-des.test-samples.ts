import { IPropertyDes, NewPropertyDes } from './property-des.model';

export const sampleWithRequiredData: IPropertyDes = {
  id: 30419,
  detail: 'Jewelery quantifying',
};

export const sampleWithPartialData: IPropertyDes = {
  id: 28883,
  detail: 'Cambridgeshire',
};

export const sampleWithFullData: IPropertyDes = {
  id: 83153,
  detail: 'Credit Usability Rustic',
};

export const sampleWithNewData: NewPropertyDes = {
  detail: 'leverage JBOD yellow',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
