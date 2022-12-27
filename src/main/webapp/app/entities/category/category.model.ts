import { IProductType } from 'app/entities/product-type/product-type.model';

export interface ICategory {
  id: number;
  name?: string | null;
  image?: string | null;
  imageContentType?: string | null;
  type?: Pick<IProductType, 'id'> | null;
}

export type NewCategory = Omit<ICategory, 'id'> & { id: null };
