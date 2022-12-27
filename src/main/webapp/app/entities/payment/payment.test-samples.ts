import dayjs from 'dayjs/esm';

import { IPayment, NewPayment } from './payment.model';

export const sampleWithRequiredData: IPayment = {
  id: 47537,
};

export const sampleWithPartialData: IPayment = {
  id: 47604,
  placedDate: dayjs('2022-12-26T11:44'),
  quantity: 88670,
};

export const sampleWithFullData: IPayment = {
  id: 46452,
  placedDate: dayjs('2022-12-26T10:37'),
  quantity: 28713,
};

export const sampleWithNewData: NewPayment = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
