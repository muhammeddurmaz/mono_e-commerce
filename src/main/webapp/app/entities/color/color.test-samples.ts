import { IColor, NewColor } from './color.model';

export const sampleWithRequiredData: IColor = {
  id: 43956,
  name: 'Sol deposit matrix',
};

export const sampleWithPartialData: IColor = {
  id: 99453,
  name: 'Chair Lodge Assurance',
};

export const sampleWithFullData: IColor = {
  id: 3182,
  name: 'Dinar Mouse Branding',
  code: 'function',
};

export const sampleWithNewData: NewColor = {
  name: 'Polarised Sausages Fish',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
