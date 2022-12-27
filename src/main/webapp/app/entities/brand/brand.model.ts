import { ISeller } from 'app/entities/seller/seller.model';

export interface IBrand {
  id: number;
  name?: string | null;
  logo?: string | null;
  logoContentType?: string | null;
  seller?: Pick<ISeller, 'id'> | null;
}

export type NewBrand = Omit<IBrand, 'id'> & { id: null };
