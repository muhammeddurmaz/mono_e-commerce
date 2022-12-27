import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUserCart, NewUserCart } from '../user-cart.model';

export type PartialUpdateUserCart = Partial<IUserCart> & Pick<IUserCart, 'id'>;

export type EntityResponseType = HttpResponse<IUserCart>;
export type EntityArrayResponseType = HttpResponse<IUserCart[]>;

@Injectable({ providedIn: 'root' })
export class UserCartService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/user-carts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(userCart: NewUserCart): Observable<EntityResponseType> {
    return this.http.post<IUserCart>(this.resourceUrl, userCart, { observe: 'response' });
  }

  update(userCart: IUserCart): Observable<EntityResponseType> {
    return this.http.put<IUserCart>(`${this.resourceUrl}/${this.getUserCartIdentifier(userCart)}`, userCart, { observe: 'response' });
  }

  partialUpdate(userCart: PartialUpdateUserCart): Observable<EntityResponseType> {
    return this.http.patch<IUserCart>(`${this.resourceUrl}/${this.getUserCartIdentifier(userCart)}`, userCart, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUserCart>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUserCart[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUserCartIdentifier(userCart: Pick<IUserCart, 'id'>): number {
    return userCart.id;
  }

  compareUserCart(o1: Pick<IUserCart, 'id'> | null, o2: Pick<IUserCart, 'id'> | null): boolean {
    return o1 && o2 ? this.getUserCartIdentifier(o1) === this.getUserCartIdentifier(o2) : o1 === o2;
  }

  addUserCartToCollectionIfMissing<Type extends Pick<IUserCart, 'id'>>(
    userCartCollection: Type[],
    ...userCartsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const userCarts: Type[] = userCartsToCheck.filter(isPresent);
    if (userCarts.length > 0) {
      const userCartCollectionIdentifiers = userCartCollection.map(userCartItem => this.getUserCartIdentifier(userCartItem)!);
      const userCartsToAdd = userCarts.filter(userCartItem => {
        const userCartIdentifier = this.getUserCartIdentifier(userCartItem);
        if (userCartCollectionIdentifiers.includes(userCartIdentifier)) {
          return false;
        }
        userCartCollectionIdentifiers.push(userCartIdentifier);
        return true;
      });
      return [...userCartsToAdd, ...userCartCollection];
    }
    return userCartCollection;
  }
}
