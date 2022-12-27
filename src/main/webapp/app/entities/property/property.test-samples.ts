import { IProperty, NewProperty } from './property.model';

export const sampleWithRequiredData: IProperty = {
  id: 21844,
  name: 'Sleek',
};

export const sampleWithPartialData: IProperty = {
  id: 28281,
  name: 'withdrawal',
};

export const sampleWithFullData: IProperty = {
  id: 87826,
  name: 'sky Account',
};

export const sampleWithNewData: NewProperty = {
  name: 'Borders',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
