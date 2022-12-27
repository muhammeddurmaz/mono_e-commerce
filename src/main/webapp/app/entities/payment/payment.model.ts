import dayjs from 'dayjs/esm';
import { IOrder } from 'app/entities/order/order.model';
import { IUser } from 'app/entities/user/user.model';
import { IUserCart } from 'app/entities/user-cart/user-cart.model';

export interface IPayment {
  id: number;
  placedDate?: dayjs.Dayjs | null;
  quantity?: number | null;
  order?: Pick<IOrder, 'id'> | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
  usercart?: Pick<IUserCart, 'id'> | null;
}

export type NewPayment = Omit<IPayment, 'id'> & { id: null };
