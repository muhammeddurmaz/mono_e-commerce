import { IUser } from 'app/entities/user/user.model';

export interface IUserAdress {
  id: number;
  name?: string | null;
  lastName?: string | null;
  telephone?: string | null;
  city?: string | null;
  adress?: string | null;
  adressTitle?: string | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewUserAdress = Omit<IUserAdress, 'id'> & { id: null };
