import { IProductImage, NewProductImage } from './product-image.model';

export const sampleWithRequiredData: IProductImage = {
  id: 61254,
  image1: '../fake-data/blob/hipster.png',
  image1ContentType: 'unknown',
};

export const sampleWithPartialData: IProductImage = {
  id: 99033,
  image1: '../fake-data/blob/hipster.png',
  image1ContentType: 'unknown',
  image2: '../fake-data/blob/hipster.png',
  image2ContentType: 'unknown',
  image3: '../fake-data/blob/hipster.png',
  image3ContentType: 'unknown',
};

export const sampleWithFullData: IProductImage = {
  id: 38281,
  image1: '../fake-data/blob/hipster.png',
  image1ContentType: 'unknown',
  image2: '../fake-data/blob/hipster.png',
  image2ContentType: 'unknown',
  image3: '../fake-data/blob/hipster.png',
  image3ContentType: 'unknown',
  image4: '../fake-data/blob/hipster.png',
  image4ContentType: 'unknown',
  image5: '../fake-data/blob/hipster.png',
  image5ContentType: 'unknown',
  image6: '../fake-data/blob/hipster.png',
  image6ContentType: 'unknown',
};

export const sampleWithNewData: NewProductImage = {
  image1: '../fake-data/blob/hipster.png',
  image1ContentType: 'unknown',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
