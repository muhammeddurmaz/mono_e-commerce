import { IProduct } from 'app/entities/product/product.model';
import { IUser } from 'app/entities/user/user.model';

export interface IFavorite {
  id: number;
  product?: Pick<IProduct, 'id'> | null;
  user?: Pick<IUser, 'id'> | null;
}

export type NewFavorite = Omit<IFavorite, 'id'> & { id: null };
