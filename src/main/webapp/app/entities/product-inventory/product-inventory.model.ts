import { IProduct } from 'app/entities/product/product.model';

export interface IProductInventory {
  id: number;
  total?: number | null;
  product?: Pick<IProduct, 'id'> | null;
}

export type NewProductInventory = Omit<IProductInventory, 'id'> & { id: null };
