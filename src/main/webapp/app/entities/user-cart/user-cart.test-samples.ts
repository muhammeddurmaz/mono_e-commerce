import { IUserCart, NewUserCart } from './user-cart.model';

export const sampleWithRequiredData: IUserCart = {
  id: 74495,
  cartName: 'state',
  cartNumber: 'parallelism Granite enable',
  name: 'Berkshire boliviano',
  lastName: 'Ayverdi',
  sktAy: 'Palladium',
  sktYil: 'Customer Buckinghamshire',
};

export const sampleWithPartialData: IUserCart = {
  id: 45619,
  cartName: 'Account Loan Circles',
  cartNumber: 'back-end Monitored',
  name: 'Uzbekistan bus Account',
  lastName: 'Tokatlıoğlu',
  sktAy: 'Incredible',
  sktYil: 'bypassing Account',
};

export const sampleWithFullData: IUserCart = {
  id: 13232,
  cartName: 'Loan Afganistan Ameliorated',
  cartNumber: 'Home',
  name: 'Soap',
  lastName: 'Doğan ',
  sktAy: 'Idaho New',
  sktYil: 'IB integrate',
};

export const sampleWithNewData: NewUserCart = {
  cartName: 'mesh',
  cartNumber: 'Beauty Trail redundant',
  name: 'Incredible',
  lastName: 'Çağıran',
  sktAy: 'Incredible input',
  sktYil: 'Berkshire overriding Analyst',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
