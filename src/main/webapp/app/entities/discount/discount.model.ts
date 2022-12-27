export interface IDiscount {
  id: number;
  title?: string | null;
  image?: string | null;
  imageContentType?: string | null;
  description?: string | null;
}

export type NewDiscount = Omit<IDiscount, 'id'> & { id: null };
