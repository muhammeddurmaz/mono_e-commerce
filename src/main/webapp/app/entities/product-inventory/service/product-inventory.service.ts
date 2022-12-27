import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProductInventory, NewProductInventory } from '../product-inventory.model';

export type PartialUpdateProductInventory = Partial<IProductInventory> & Pick<IProductInventory, 'id'>;

export type EntityResponseType = HttpResponse<IProductInventory>;
export type EntityArrayResponseType = HttpResponse<IProductInventory[]>;

@Injectable({ providedIn: 'root' })
export class ProductInventoryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/product-inventories');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(productInventory: NewProductInventory): Observable<EntityResponseType> {
    return this.http.post<IProductInventory>(this.resourceUrl, productInventory, { observe: 'response' });
  }

  update(productInventory: IProductInventory): Observable<EntityResponseType> {
    return this.http.put<IProductInventory>(
      `${this.resourceUrl}/${this.getProductInventoryIdentifier(productInventory)}`,
      productInventory,
      { observe: 'response' }
    );
  }

  partialUpdate(productInventory: PartialUpdateProductInventory): Observable<EntityResponseType> {
    return this.http.patch<IProductInventory>(
      `${this.resourceUrl}/${this.getProductInventoryIdentifier(productInventory)}`,
      productInventory,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProductInventory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProductInventory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProductInventoryIdentifier(productInventory: Pick<IProductInventory, 'id'>): number {
    return productInventory.id;
  }

  compareProductInventory(o1: Pick<IProductInventory, 'id'> | null, o2: Pick<IProductInventory, 'id'> | null): boolean {
    return o1 && o2 ? this.getProductInventoryIdentifier(o1) === this.getProductInventoryIdentifier(o2) : o1 === o2;
  }

  addProductInventoryToCollectionIfMissing<Type extends Pick<IProductInventory, 'id'>>(
    productInventoryCollection: Type[],
    ...productInventoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productInventories: Type[] = productInventoriesToCheck.filter(isPresent);
    if (productInventories.length > 0) {
      const productInventoryCollectionIdentifiers = productInventoryCollection.map(
        productInventoryItem => this.getProductInventoryIdentifier(productInventoryItem)!
      );
      const productInventoriesToAdd = productInventories.filter(productInventoryItem => {
        const productInventoryIdentifier = this.getProductInventoryIdentifier(productInventoryItem);
        if (productInventoryCollectionIdentifiers.includes(productInventoryIdentifier)) {
          return false;
        }
        productInventoryCollectionIdentifiers.push(productInventoryIdentifier);
        return true;
      });
      return [...productInventoriesToAdd, ...productInventoryCollection];
    }
    return productInventoryCollection;
  }
}
