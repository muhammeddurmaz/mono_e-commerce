import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ISellerStatistics, NewSellerStatistics } from '../seller-statistics.model';

export type PartialUpdateSellerStatistics = Partial<ISellerStatistics> & Pick<ISellerStatistics, 'id'>;

export type EntityResponseType = HttpResponse<ISellerStatistics>;
export type EntityArrayResponseType = HttpResponse<ISellerStatistics[]>;

@Injectable({ providedIn: 'root' })
export class SellerStatisticsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/seller-statistics');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/seller-statistics');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISellerStatistics>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISellerStatistics[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISellerStatistics[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  getSellerStatisticsIdentifier(sellerStatistics: Pick<ISellerStatistics, 'id'>): number {
    return sellerStatistics.id;
  }

  compareSellerStatistics(o1: Pick<ISellerStatistics, 'id'> | null, o2: Pick<ISellerStatistics, 'id'> | null): boolean {
    return o1 && o2 ? this.getSellerStatisticsIdentifier(o1) === this.getSellerStatisticsIdentifier(o2) : o1 === o2;
  }

  addSellerStatisticsToCollectionIfMissing<Type extends Pick<ISellerStatistics, 'id'>>(
    sellerStatisticsCollection: Type[],
    ...sellerStatisticsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const sellerStatistics: Type[] = sellerStatisticsToCheck.filter(isPresent);
    if (sellerStatistics.length > 0) {
      const sellerStatisticsCollectionIdentifiers = sellerStatisticsCollection.map(
        sellerStatisticsItem => this.getSellerStatisticsIdentifier(sellerStatisticsItem)!
      );
      const sellerStatisticsToAdd = sellerStatistics.filter(sellerStatisticsItem => {
        const sellerStatisticsIdentifier = this.getSellerStatisticsIdentifier(sellerStatisticsItem);
        if (sellerStatisticsCollectionIdentifiers.includes(sellerStatisticsIdentifier)) {
          return false;
        }
        sellerStatisticsCollectionIdentifiers.push(sellerStatisticsIdentifier);
        return true;
      });
      return [...sellerStatisticsToAdd, ...sellerStatisticsCollection];
    }
    return sellerStatisticsCollection;
  }
}
