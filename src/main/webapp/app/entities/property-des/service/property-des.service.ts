import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IPropertyDes, NewPropertyDes } from '../property-des.model';

export type PartialUpdatePropertyDes = Partial<IPropertyDes> & Pick<IPropertyDes, 'id'>;

export type EntityResponseType = HttpResponse<IPropertyDes>;
export type EntityArrayResponseType = HttpResponse<IPropertyDes[]>;

@Injectable({ providedIn: 'root' })
export class PropertyDesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/property-des');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/property-des');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(propertyDes: NewPropertyDes): Observable<EntityResponseType> {
    return this.http.post<IPropertyDes>(this.resourceUrl, propertyDes, { observe: 'response' });
  }

  update(propertyDes: IPropertyDes): Observable<EntityResponseType> {
    return this.http.put<IPropertyDes>(`${this.resourceUrl}/${this.getPropertyDesIdentifier(propertyDes)}`, propertyDes, {
      observe: 'response',
    });
  }

  partialUpdate(propertyDes: PartialUpdatePropertyDes): Observable<EntityResponseType> {
    return this.http.patch<IPropertyDes>(`${this.resourceUrl}/${this.getPropertyDesIdentifier(propertyDes)}`, propertyDes, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPropertyDes>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPropertyDes[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPropertyDes[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  getPropertyDesIdentifier(propertyDes: Pick<IPropertyDes, 'id'>): number {
    return propertyDes.id;
  }

  comparePropertyDes(o1: Pick<IPropertyDes, 'id'> | null, o2: Pick<IPropertyDes, 'id'> | null): boolean {
    return o1 && o2 ? this.getPropertyDesIdentifier(o1) === this.getPropertyDesIdentifier(o2) : o1 === o2;
  }

  addPropertyDesToCollectionIfMissing<Type extends Pick<IPropertyDes, 'id'>>(
    propertyDesCollection: Type[],
    ...propertyDesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const propertyDes: Type[] = propertyDesToCheck.filter(isPresent);
    if (propertyDes.length > 0) {
      const propertyDesCollectionIdentifiers = propertyDesCollection.map(
        propertyDesItem => this.getPropertyDesIdentifier(propertyDesItem)!
      );
      const propertyDesToAdd = propertyDes.filter(propertyDesItem => {
        const propertyDesIdentifier = this.getPropertyDesIdentifier(propertyDesItem);
        if (propertyDesCollectionIdentifiers.includes(propertyDesIdentifier)) {
          return false;
        }
        propertyDesCollectionIdentifiers.push(propertyDesIdentifier);
        return true;
      });
      return [...propertyDesToAdd, ...propertyDesCollection];
    }
    return propertyDesCollection;
  }
}
