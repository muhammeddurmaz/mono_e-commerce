import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IProductType } from 'app/entities/product-type/product-type.model';

export interface ISeller {
  id: number;
  name?: string | null;
  lastName?: string | null;
  shopName?: string | null;
  mail?: string | null;
  activated?: boolean | null;
  image?: string | null;
  imageContentType?: string | null;
  tckn?: string | null;
  phone?: string | null;
  city?: string | null;
  placedDate?: dayjs.Dayjs | null;
  user?: Pick<IUser, 'id'> | null;
  sellerProductType?: Pick<IProductType, 'id'> | null;
}

export type NewSeller = Omit<ISeller, 'id'> & { id: null };
