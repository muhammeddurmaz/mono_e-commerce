import dayjs from 'dayjs/esm';
import { IUserAdress } from 'app/entities/user-adress/user-adress.model';
import { IUser } from 'app/entities/user/user.model';
import { OrderStatus } from 'app/entities/enumerations/order-status.model';

export interface IOrder {
  id: number;
  placedDate?: dayjs.Dayjs | null;
  totalQuantity?: number | null;
  totalPrice?: number | null;
  status?: OrderStatus | null;
  adress?: Pick<IUserAdress, 'id'> | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewOrder = Omit<IOrder, 'id'> & { id: null };
