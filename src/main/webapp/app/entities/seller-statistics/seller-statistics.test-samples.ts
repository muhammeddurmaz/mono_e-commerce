import { ISellerStatistics, NewSellerStatistics } from './seller-statistics.model';

export const sampleWithRequiredData: ISellerStatistics = {
  id: 94956,
};

export const sampleWithPartialData: ISellerStatistics = {
  id: 99538,
  product: 89144,
  totalOrder: 86760,
};

export const sampleWithFullData: ISellerStatistics = {
  id: 86980,
  product: 83066,
  totalOrder: 5367,
  totalEarning: 93747,
};

export const sampleWithNewData: NewSellerStatistics = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
