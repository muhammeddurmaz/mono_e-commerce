import { IUser } from 'app/entities/user/user.model';
import { IProduct } from 'app/entities/product/product.model';
import { Rating } from 'app/entities/enumerations/rating.model';

export interface IComment {
  id: number;
  description?: string | null;
  image?: string | null;
  imageContentType?: string | null;
  rating?: Rating | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
  product?: Pick<IProduct, 'id'> | null;
}

export type NewComment = Omit<IComment, 'id'> & { id: null };
