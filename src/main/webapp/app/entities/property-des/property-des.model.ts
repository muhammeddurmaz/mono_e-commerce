import { IProperty } from 'app/entities/property/property.model';
import { IProduct } from 'app/entities/product/product.model';

export interface IPropertyDes {
  id: number;
  detail?: string | null;
  property?: Pick<IProperty, 'id'> | null;
  product?: Pick<IProduct, 'id'> | null;
}

export type NewPropertyDes = Omit<IPropertyDes, 'id'> & { id: null };
