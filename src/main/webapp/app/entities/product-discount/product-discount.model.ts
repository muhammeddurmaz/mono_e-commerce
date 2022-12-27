import dayjs from 'dayjs/esm';
import { IDiscount } from 'app/entities/discount/discount.model';
import { IProduct } from 'app/entities/product/product.model';

export interface IProductDiscount {
  id: number;
  addedDate?: dayjs.Dayjs | null;
  dueDate?: dayjs.Dayjs | null;
  description?: string | null;
  discount?: Pick<IDiscount, 'id'> | null;
  product?: Pick<IProduct, 'id'> | null;
}

export type NewProductDiscount = Omit<IProductDiscount, 'id'> & { id: null };
