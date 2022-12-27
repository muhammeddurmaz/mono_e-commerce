export interface IProductType {
  id: number;
  name?: string | null;
}

export type NewProductType = Omit<IProductType, 'id'> & { id: null };
