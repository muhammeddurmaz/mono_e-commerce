import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IFavorite, NewFavorite } from '../favorite.model';

export type PartialUpdateFavorite = Partial<IFavorite> & Pick<IFavorite, 'id'>;

export type EntityResponseType = HttpResponse<IFavorite>;
export type EntityArrayResponseType = HttpResponse<IFavorite[]>;

@Injectable({ providedIn: 'root' })
export class FavoriteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/favorites');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/favorites');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(favorite: NewFavorite): Observable<EntityResponseType> {
    return this.http.post<IFavorite>(this.resourceUrl, favorite, { observe: 'response' });
  }

  update(favorite: IFavorite): Observable<EntityResponseType> {
    return this.http.put<IFavorite>(`${this.resourceUrl}/${this.getFavoriteIdentifier(favorite)}`, favorite, { observe: 'response' });
  }

  partialUpdate(favorite: PartialUpdateFavorite): Observable<EntityResponseType> {
    return this.http.patch<IFavorite>(`${this.resourceUrl}/${this.getFavoriteIdentifier(favorite)}`, favorite, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFavorite>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFavorite[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFavorite[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  getFavoriteIdentifier(favorite: Pick<IFavorite, 'id'>): number {
    return favorite.id;
  }

  compareFavorite(o1: Pick<IFavorite, 'id'> | null, o2: Pick<IFavorite, 'id'> | null): boolean {
    return o1 && o2 ? this.getFavoriteIdentifier(o1) === this.getFavoriteIdentifier(o2) : o1 === o2;
  }

  addFavoriteToCollectionIfMissing<Type extends Pick<IFavorite, 'id'>>(
    favoriteCollection: Type[],
    ...favoritesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const favorites: Type[] = favoritesToCheck.filter(isPresent);
    if (favorites.length > 0) {
      const favoriteCollectionIdentifiers = favoriteCollection.map(favoriteItem => this.getFavoriteIdentifier(favoriteItem)!);
      const favoritesToAdd = favorites.filter(favoriteItem => {
        const favoriteIdentifier = this.getFavoriteIdentifier(favoriteItem);
        if (favoriteCollectionIdentifiers.includes(favoriteIdentifier)) {
          return false;
        }
        favoriteCollectionIdentifiers.push(favoriteIdentifier);
        return true;
      });
      return [...favoritesToAdd, ...favoriteCollection];
    }
    return favoriteCollection;
  }
}
