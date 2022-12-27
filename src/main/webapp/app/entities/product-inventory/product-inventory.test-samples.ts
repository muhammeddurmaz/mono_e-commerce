import { IProductInventory, NewProductInventory } from './product-inventory.model';

export const sampleWithRequiredData: IProductInventory = {
  id: 46555,
  total: 39102,
};

export const sampleWithPartialData: IProductInventory = {
  id: 42165,
  total: 72306,
};

export const sampleWithFullData: IProductInventory = {
  id: 20184,
  total: 7286,
};

export const sampleWithNewData: NewProductInventory = {
  total: 90753,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
