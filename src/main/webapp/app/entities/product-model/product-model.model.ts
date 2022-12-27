export interface IProductModel {
  id: number;
  modelCode?: string | null;
}

export type NewProductModel = Omit<IProductModel, 'id'> & { id: null };
