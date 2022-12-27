import dayjs from 'dayjs/esm';

import { ISeller, NewSeller } from './seller.model';

export const sampleWithRequiredData: ISeller = {
  id: 23525,
  name: 'IB Checking Jersey',
  lastName: 'Bademci',
  shopName: 'Gorgeous Metrics',
  mail: 'deposit 5th back-end',
  tckn: 'disintermediate',
  phone: '+90-409-060-45-88',
  city: 'Tüzünland',
};

export const sampleWithPartialData: ISeller = {
  id: 59315,
  name: 'Land Hat',
  lastName: 'Avan',
  shopName: 'Kentucky Leone Pizza',
  mail: 'salmon fuchsia digital',
  activated: true,
  image: '../fake-data/blob/hipster.png',
  imageContentType: 'unknown',
  tckn: 'discrete Intranet panel',
  phone: '+90-584-654-0-029',
  city: 'Pittsburgh',
  placedDate: dayjs('2022-12-26T02:26'),
};

export const sampleWithFullData: ISeller = {
  id: 94779,
  name: 'Jamaican',
  lastName: 'Ertürk',
  shopName: 'Principal Baby deposit',
  mail: 'Mouse AI',
  activated: false,
  image: '../fake-data/blob/hipster.png',
  imageContentType: 'unknown',
  tckn: 'SupervisorX',
  phone: '+90-703-853-5-652',
  city: 'Köylüoğlufort',
  placedDate: dayjs('2022-12-25T20:49'),
};

export const sampleWithNewData: NewSeller = {
  name: 'programming hack Customer',
  lastName: 'Öymen',
  shopName: 'Practical District Facilitator',
  mail: 'Stream mobile',
  tckn: 'Bedfordshire Integration',
  phone: '+90-857-027-93-47',
  city: 'Port Altıntamganport',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
