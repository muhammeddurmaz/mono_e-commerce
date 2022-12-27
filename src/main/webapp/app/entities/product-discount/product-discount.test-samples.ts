import dayjs from 'dayjs/esm';

import { IProductDiscount, NewProductDiscount } from './product-discount.model';

export const sampleWithRequiredData: IProductDiscount = {
  id: 46922,
  addedDate: dayjs('2022-12-26T12:30'),
  dueDate: dayjs('2022-12-26T09:52'),
};

export const sampleWithPartialData: IProductDiscount = {
  id: 58036,
  addedDate: dayjs('2022-12-25T21:30'),
  dueDate: dayjs('2022-12-25T22:59'),
};

export const sampleWithFullData: IProductDiscount = {
  id: 62392,
  addedDate: dayjs('2022-12-26T18:05'),
  dueDate: dayjs('2022-12-25T21:11'),
  description: 'innovative',
};

export const sampleWithNewData: NewProductDiscount = {
  addedDate: dayjs('2022-12-26T09:23'),
  dueDate: dayjs('2022-12-26T16:13'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
