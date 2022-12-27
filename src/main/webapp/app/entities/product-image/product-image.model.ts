import { IProduct } from 'app/entities/product/product.model';

export interface IProductImage {
  id: number;
  image1?: string | null;
  image1ContentType?: string | null;
  image2?: string | null;
  image2ContentType?: string | null;
  image3?: string | null;
  image3ContentType?: string | null;
  image4?: string | null;
  image4ContentType?: string | null;
  image5?: string | null;
  image5ContentType?: string | null;
  image6?: string | null;
  image6ContentType?: string | null;
  product?: Pick<IProduct, 'id'> | null;
}

export type NewProductImage = Omit<IProductImage, 'id'> & { id: null };
