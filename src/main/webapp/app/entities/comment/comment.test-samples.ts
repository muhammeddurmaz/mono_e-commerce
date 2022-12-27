import { Rating } from 'app/entities/enumerations/rating.model';

import { IComment, NewComment } from './comment.model';

export const sampleWithRequiredData: IComment = {
  id: 78899,
  description: '../fake-data/blob/hipster.txt',
};

export const sampleWithPartialData: IComment = {
  id: 38860,
  description: '../fake-data/blob/hipster.txt',
  rating: Rating['THREESTAR'],
};

export const sampleWithFullData: IComment = {
  id: 78272,
  description: '../fake-data/blob/hipster.txt',
  image: '../fake-data/blob/hipster.png',
  imageContentType: 'unknown',
  rating: Rating['TWOSTAR'],
};

export const sampleWithNewData: NewComment = {
  description: '../fake-data/blob/hipster.txt',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
