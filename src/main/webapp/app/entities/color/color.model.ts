export interface IColor {
  id: number;
  name?: string | null;
  code?: string | null;
}

export type NewColor = Omit<IColor, 'id'> & { id: null };
