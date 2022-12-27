import dayjs from 'dayjs/esm';

import { IProduct, NewProduct } from './product.model';

export const sampleWithRequiredData: IProduct = {
  id: 77672,
  barcode: 'Customer-focused',
  modelCode: 'cross-platform Unbranded deposit',
  name: 'Interface Table',
  price: 304,
  image: '../fake-data/blob/hipster.png',
  imageContentType: 'unknown',
  addedDate: dayjs('2022-12-26T09:08'),
  stock: 82094,
};

export const sampleWithPartialData: IProduct = {
  id: 62539,
  barcode: 'District',
  modelCode: 'Garden Customer-focused Island',
  name: 'mission-critical',
  price: 32091,
  discountPrice: 19367,
  description: '../fake-data/blob/hipster.txt',
  image: '../fake-data/blob/hipster.png',
  imageContentType: 'unknown',
  addedDate: dayjs('2022-12-25T20:20'),
  rating: 90500,
  sizee: 'Islands',
  stock: 70955,
  active: false,
};

export const sampleWithFullData: IProduct = {
  id: 3170,
  barcode: 'policy neural',
  modelCode: 'intuitive SDR',
  name: 'Intelligent structure lime',
  price: 44267,
  discountPrice: 41457,
  description: '../fake-data/blob/hipster.txt',
  image: '../fake-data/blob/hipster.png',
  imageContentType: 'unknown',
  addedDate: dayjs('2022-12-26T04:32'),
  rating: 30260,
  sizee: 'architectures enhance',
  stock: 50032,
  active: false,
};

export const sampleWithNewData: NewProduct = {
  barcode: 'payment core compress',
  modelCode: 'Operations pixel Pound',
  name: 'service-desk Salad Dollar',
  price: 1626,
  image: '../fake-data/blob/hipster.png',
  imageContentType: 'unknown',
  addedDate: dayjs('2022-12-26T08:35'),
  stock: 68114,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
