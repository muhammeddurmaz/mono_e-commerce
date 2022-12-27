import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IProductDiscount, NewProductDiscount } from '../product-discount.model';

export type PartialUpdateProductDiscount = Partial<IProductDiscount> & Pick<IProductDiscount, 'id'>;

type RestOf<T extends IProductDiscount | NewProductDiscount> = Omit<T, 'addedDate' | 'dueDate'> & {
  addedDate?: string | null;
  dueDate?: string | null;
};

export type RestProductDiscount = RestOf<IProductDiscount>;

export type NewRestProductDiscount = RestOf<NewProductDiscount>;

export type PartialUpdateRestProductDiscount = RestOf<PartialUpdateProductDiscount>;

export type EntityResponseType = HttpResponse<IProductDiscount>;
export type EntityArrayResponseType = HttpResponse<IProductDiscount[]>;

@Injectable({ providedIn: 'root' })
export class ProductDiscountService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/product-discounts');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/product-discounts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(productDiscount: NewProductDiscount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productDiscount);
    return this.http
      .post<RestProductDiscount>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(productDiscount: IProductDiscount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productDiscount);
    return this.http
      .put<RestProductDiscount>(`${this.resourceUrl}/${this.getProductDiscountIdentifier(productDiscount)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(productDiscount: PartialUpdateProductDiscount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productDiscount);
    return this.http
      .patch<RestProductDiscount>(`${this.resourceUrl}/${this.getProductDiscountIdentifier(productDiscount)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestProductDiscount>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestProductDiscount[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestProductDiscount[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  getProductDiscountIdentifier(productDiscount: Pick<IProductDiscount, 'id'>): number {
    return productDiscount.id;
  }

  compareProductDiscount(o1: Pick<IProductDiscount, 'id'> | null, o2: Pick<IProductDiscount, 'id'> | null): boolean {
    return o1 && o2 ? this.getProductDiscountIdentifier(o1) === this.getProductDiscountIdentifier(o2) : o1 === o2;
  }

  addProductDiscountToCollectionIfMissing<Type extends Pick<IProductDiscount, 'id'>>(
    productDiscountCollection: Type[],
    ...productDiscountsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productDiscounts: Type[] = productDiscountsToCheck.filter(isPresent);
    if (productDiscounts.length > 0) {
      const productDiscountCollectionIdentifiers = productDiscountCollection.map(
        productDiscountItem => this.getProductDiscountIdentifier(productDiscountItem)!
      );
      const productDiscountsToAdd = productDiscounts.filter(productDiscountItem => {
        const productDiscountIdentifier = this.getProductDiscountIdentifier(productDiscountItem);
        if (productDiscountCollectionIdentifiers.includes(productDiscountIdentifier)) {
          return false;
        }
        productDiscountCollectionIdentifiers.push(productDiscountIdentifier);
        return true;
      });
      return [...productDiscountsToAdd, ...productDiscountCollection];
    }
    return productDiscountCollection;
  }

  protected convertDateFromClient<T extends IProductDiscount | NewProductDiscount | PartialUpdateProductDiscount>(
    productDiscount: T
  ): RestOf<T> {
    return {
      ...productDiscount,
      addedDate: productDiscount.addedDate?.toJSON() ?? null,
      dueDate: productDiscount.dueDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restProductDiscount: RestProductDiscount): IProductDiscount {
    return {
      ...restProductDiscount,
      addedDate: restProductDiscount.addedDate ? dayjs(restProductDiscount.addedDate) : undefined,
      dueDate: restProductDiscount.dueDate ? dayjs(restProductDiscount.dueDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestProductDiscount>): HttpResponse<IProductDiscount> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestProductDiscount[]>): HttpResponse<IProductDiscount[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
