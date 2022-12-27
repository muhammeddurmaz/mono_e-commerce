import { OrderItemStatus } from 'app/entities/enumerations/order-item-status.model';

import { IOrderItem, NewOrderItem } from './order-item.model';

export const sampleWithRequiredData: IOrderItem = {
  id: 62496,
  quantity: 85505,
  totalPrice: 70793,
};

export const sampleWithPartialData: IOrderItem = {
  id: 61163,
  quantity: 15882,
  totalPrice: 50845,
};

export const sampleWithFullData: IOrderItem = {
  id: 74213,
  quantity: 67235,
  totalPrice: 54419,
  status: OrderItemStatus['PENDING'],
};

export const sampleWithNewData: NewOrderItem = {
  quantity: 60042,
  totalPrice: 58546,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
