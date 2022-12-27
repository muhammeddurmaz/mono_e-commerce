import { ISeller } from 'app/entities/seller/seller.model';

export interface ISellerStatistics {
  id: number;
  product?: number | null;
  totalOrder?: number | null;
  totalEarning?: number | null;
  seller?: Pick<ISeller, 'id'> | null;
}

export type NewSellerStatistics = Omit<ISellerStatistics, 'id'> & { id: null };
