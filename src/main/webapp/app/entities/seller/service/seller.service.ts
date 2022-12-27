import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ISeller, NewSeller } from '../seller.model';

export type PartialUpdateSeller = Partial<ISeller> & Pick<ISeller, 'id'>;

type RestOf<T extends ISeller | NewSeller> = Omit<T, 'placedDate'> & {
  placedDate?: string | null;
};

export type RestSeller = RestOf<ISeller>;

export type NewRestSeller = RestOf<NewSeller>;

export type PartialUpdateRestSeller = RestOf<PartialUpdateSeller>;

export type EntityResponseType = HttpResponse<ISeller>;
export type EntityArrayResponseType = HttpResponse<ISeller[]>;

@Injectable({ providedIn: 'root' })
export class SellerService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sellers');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/sellers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(seller: NewSeller): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(seller);
    return this.http
      .post<RestSeller>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(seller: ISeller): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(seller);
    return this.http
      .put<RestSeller>(`${this.resourceUrl}/${this.getSellerIdentifier(seller)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(seller: PartialUpdateSeller): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(seller);
    return this.http
      .patch<RestSeller>(`${this.resourceUrl}/${this.getSellerIdentifier(seller)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSeller>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSeller[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSeller[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  getSellerIdentifier(seller: Pick<ISeller, 'id'>): number {
    return seller.id;
  }

  compareSeller(o1: Pick<ISeller, 'id'> | null, o2: Pick<ISeller, 'id'> | null): boolean {
    return o1 && o2 ? this.getSellerIdentifier(o1) === this.getSellerIdentifier(o2) : o1 === o2;
  }

  addSellerToCollectionIfMissing<Type extends Pick<ISeller, 'id'>>(
    sellerCollection: Type[],
    ...sellersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const sellers: Type[] = sellersToCheck.filter(isPresent);
    if (sellers.length > 0) {
      const sellerCollectionIdentifiers = sellerCollection.map(sellerItem => this.getSellerIdentifier(sellerItem)!);
      const sellersToAdd = sellers.filter(sellerItem => {
        const sellerIdentifier = this.getSellerIdentifier(sellerItem);
        if (sellerCollectionIdentifiers.includes(sellerIdentifier)) {
          return false;
        }
        sellerCollectionIdentifiers.push(sellerIdentifier);
        return true;
      });
      return [...sellersToAdd, ...sellerCollection];
    }
    return sellerCollection;
  }

  protected convertDateFromClient<T extends ISeller | NewSeller | PartialUpdateSeller>(seller: T): RestOf<T> {
    return {
      ...seller,
      placedDate: seller.placedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restSeller: RestSeller): ISeller {
    return {
      ...restSeller,
      placedDate: restSeller.placedDate ? dayjs(restSeller.placedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSeller>): HttpResponse<ISeller> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSeller[]>): HttpResponse<ISeller[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
