import { ICategory } from 'app/entities/category/category.model';

export interface ISubCategory {
  id: number;
  name?: string | null;
  image?: string | null;
  imageContentType?: string | null;
  category?: Pick<ICategory, 'id'> | null;
}

export type NewSubCategory = Omit<ISubCategory, 'id'> & { id: null };
