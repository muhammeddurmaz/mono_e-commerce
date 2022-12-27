import { IUser } from 'app/entities/user/user.model';

export interface IUserCart {
  id: number;
  cartName?: string | null;
  cartNumber?: string | null;
  name?: string | null;
  lastName?: string | null;
  sktAy?: string | null;
  sktYil?: string | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewUserCart = Omit<IUserCart, 'id'> & { id: null };
