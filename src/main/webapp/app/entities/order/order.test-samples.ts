import dayjs from 'dayjs/esm';

import { OrderStatus } from 'app/entities/enumerations/order-status.model';

import { IOrder, NewOrder } from './order.model';

export const sampleWithRequiredData: IOrder = {
  id: 47761,
  placedDate: dayjs('2022-12-26T11:51'),
  totalQuantity: 70907,
  totalPrice: 52696,
};

export const sampleWithPartialData: IOrder = {
  id: 34054,
  placedDate: dayjs('2022-12-26T08:50'),
  totalQuantity: 56605,
  totalPrice: 84971,
  status: OrderStatus['COMPLETED'],
};

export const sampleWithFullData: IOrder = {
  id: 68170,
  placedDate: dayjs('2022-12-25T21:39'),
  totalQuantity: 61522,
  totalPrice: 3810,
  status: OrderStatus['CANCELLED'],
};

export const sampleWithNewData: NewOrder = {
  placedDate: dayjs('2022-12-26T09:22'),
  totalQuantity: 95875,
  totalPrice: 44107,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
