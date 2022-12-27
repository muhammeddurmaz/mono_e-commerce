import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProductStatistics, NewProductStatistics } from '../product-statistics.model';

export type PartialUpdateProductStatistics = Partial<IProductStatistics> & Pick<IProductStatistics, 'id'>;

export type EntityResponseType = HttpResponse<IProductStatistics>;
export type EntityArrayResponseType = HttpResponse<IProductStatistics[]>;

@Injectable({ providedIn: 'root' })
export class ProductStatisticsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/product-statistics');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProductStatistics>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProductStatistics[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  getProductStatisticsIdentifier(productStatistics: Pick<IProductStatistics, 'id'>): number {
    return productStatistics.id;
  }

  compareProductStatistics(o1: Pick<IProductStatistics, 'id'> | null, o2: Pick<IProductStatistics, 'id'> | null): boolean {
    return o1 && o2 ? this.getProductStatisticsIdentifier(o1) === this.getProductStatisticsIdentifier(o2) : o1 === o2;
  }

  addProductStatisticsToCollectionIfMissing<Type extends Pick<IProductStatistics, 'id'>>(
    productStatisticsCollection: Type[],
    ...productStatisticsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productStatistics: Type[] = productStatisticsToCheck.filter(isPresent);
    if (productStatistics.length > 0) {
      const productStatisticsCollectionIdentifiers = productStatisticsCollection.map(
        productStatisticsItem => this.getProductStatisticsIdentifier(productStatisticsItem)!
      );
      const productStatisticsToAdd = productStatistics.filter(productStatisticsItem => {
        const productStatisticsIdentifier = this.getProductStatisticsIdentifier(productStatisticsItem);
        if (productStatisticsCollectionIdentifiers.includes(productStatisticsIdentifier)) {
          return false;
        }
        productStatisticsCollectionIdentifiers.push(productStatisticsIdentifier);
        return true;
      });
      return [...productStatisticsToAdd, ...productStatisticsCollection];
    }
    return productStatisticsCollection;
  }
}
