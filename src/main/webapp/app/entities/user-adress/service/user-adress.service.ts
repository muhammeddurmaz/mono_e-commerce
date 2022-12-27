import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUserAdress, NewUserAdress } from '../user-adress.model';

export type PartialUpdateUserAdress = Partial<IUserAdress> & Pick<IUserAdress, 'id'>;

export type EntityResponseType = HttpResponse<IUserAdress>;
export type EntityArrayResponseType = HttpResponse<IUserAdress[]>;

@Injectable({ providedIn: 'root' })
export class UserAdressService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/user-adresses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(userAdress: NewUserAdress): Observable<EntityResponseType> {
    return this.http.post<IUserAdress>(this.resourceUrl, userAdress, { observe: 'response' });
  }

  update(userAdress: IUserAdress): Observable<EntityResponseType> {
    return this.http.put<IUserAdress>(`${this.resourceUrl}/${this.getUserAdressIdentifier(userAdress)}`, userAdress, {
      observe: 'response',
    });
  }

  partialUpdate(userAdress: PartialUpdateUserAdress): Observable<EntityResponseType> {
    return this.http.patch<IUserAdress>(`${this.resourceUrl}/${this.getUserAdressIdentifier(userAdress)}`, userAdress, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUserAdress>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUserAdress[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUserAdressIdentifier(userAdress: Pick<IUserAdress, 'id'>): number {
    return userAdress.id;
  }

  compareUserAdress(o1: Pick<IUserAdress, 'id'> | null, o2: Pick<IUserAdress, 'id'> | null): boolean {
    return o1 && o2 ? this.getUserAdressIdentifier(o1) === this.getUserAdressIdentifier(o2) : o1 === o2;
  }

  addUserAdressToCollectionIfMissing<Type extends Pick<IUserAdress, 'id'>>(
    userAdressCollection: Type[],
    ...userAdressesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const userAdresses: Type[] = userAdressesToCheck.filter(isPresent);
    if (userAdresses.length > 0) {
      const userAdressCollectionIdentifiers = userAdressCollection.map(userAdressItem => this.getUserAdressIdentifier(userAdressItem)!);
      const userAdressesToAdd = userAdresses.filter(userAdressItem => {
        const userAdressIdentifier = this.getUserAdressIdentifier(userAdressItem);
        if (userAdressCollectionIdentifiers.includes(userAdressIdentifier)) {
          return false;
        }
        userAdressCollectionIdentifiers.push(userAdressIdentifier);
        return true;
      });
      return [...userAdressesToAdd, ...userAdressCollection];
    }
    return userAdressCollection;
  }
}
