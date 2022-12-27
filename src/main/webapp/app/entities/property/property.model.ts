import { ICategory } from 'app/entities/category/category.model';

export interface IProperty {
  id: number;
  name?: string | null;
  category?: Pick<ICategory, 'id'> | null;
}

export type NewProperty = Omit<IProperty, 'id'> & { id: null };
