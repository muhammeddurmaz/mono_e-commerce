import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProductModel, NewProductModel } from '../product-model.model';

export type PartialUpdateProductModel = Partial<IProductModel> & Pick<IProductModel, 'id'>;

export type EntityResponseType = HttpResponse<IProductModel>;
export type EntityArrayResponseType = HttpResponse<IProductModel[]>;

@Injectable({ providedIn: 'root' })
export class ProductModelService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/product-models');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(productModel: NewProductModel): Observable<EntityResponseType> {
    return this.http.post<IProductModel>(this.resourceUrl, productModel, { observe: 'response' });
  }

  update(productModel: IProductModel): Observable<EntityResponseType> {
    return this.http.put<IProductModel>(`${this.resourceUrl}/${this.getProductModelIdentifier(productModel)}`, productModel, {
      observe: 'response',
    });
  }

  partialUpdate(productModel: PartialUpdateProductModel): Observable<EntityResponseType> {
    return this.http.patch<IProductModel>(`${this.resourceUrl}/${this.getProductModelIdentifier(productModel)}`, productModel, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProductModel>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProductModel[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProductModelIdentifier(productModel: Pick<IProductModel, 'id'>): number {
    return productModel.id;
  }

  compareProductModel(o1: Pick<IProductModel, 'id'> | null, o2: Pick<IProductModel, 'id'> | null): boolean {
    return o1 && o2 ? this.getProductModelIdentifier(o1) === this.getProductModelIdentifier(o2) : o1 === o2;
  }

  addProductModelToCollectionIfMissing<Type extends Pick<IProductModel, 'id'>>(
    productModelCollection: Type[],
    ...productModelsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productModels: Type[] = productModelsToCheck.filter(isPresent);
    if (productModels.length > 0) {
      const productModelCollectionIdentifiers = productModelCollection.map(
        productModelItem => this.getProductModelIdentifier(productModelItem)!
      );
      const productModelsToAdd = productModels.filter(productModelItem => {
        const productModelIdentifier = this.getProductModelIdentifier(productModelItem);
        if (productModelCollectionIdentifiers.includes(productModelIdentifier)) {
          return false;
        }
        productModelCollectionIdentifiers.push(productModelIdentifier);
        return true;
      });
      return [...productModelsToAdd, ...productModelCollection];
    }
    return productModelCollection;
  }
}
