import dayjs from 'dayjs/esm';
import { ICategory } from 'app/entities/category/category.model';
import { ISubCategory } from 'app/entities/sub-category/sub-category.model';
import { IColor } from 'app/entities/color/color.model';
import { IProductModel } from 'app/entities/product-model/product-model.model';
import { ISeller } from 'app/entities/seller/seller.model';
import { IBrand } from 'app/entities/brand/brand.model';

export interface IProduct {
  id: number;
  barcode?: string | null;
  modelCode?: string | null;
  name?: string | null;
  price?: number | null;
  discountPrice?: number | null;
  description?: string | null;
  image?: string | null;
  imageContentType?: string | null;
  addedDate?: dayjs.Dayjs | null;
  rating?: number | null;
  sizee?: string | null;
  stock?: number | null;
  active?: boolean | null;
  category?: Pick<ICategory, 'id'> | null;
  subCategory?: Pick<ISubCategory, 'id'> | null;
  color?: Pick<IColor, 'id'> | null;
  productModel?: Pick<IProductModel, 'id'> | null;
  seller?: Pick<ISeller, 'id'> | null;
  brand?: Pick<IBrand, 'id'> | null;
}

export type NewProduct = Omit<IProduct, 'id'> & { id: null };
