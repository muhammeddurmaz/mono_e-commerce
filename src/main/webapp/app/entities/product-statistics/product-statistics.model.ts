import { IProduct } from 'app/entities/product/product.model';

export interface IProductStatistics {
  id: number;
  order?: number | null;
  click?: number | null;
  comment?: number | null;
  rating?: number | null;
  addCart?: number | null;
  product?: Pick<IProduct, 'id'> | null;
}

export type NewProductStatistics = Omit<IProductStatistics, 'id'> & { id: null };
