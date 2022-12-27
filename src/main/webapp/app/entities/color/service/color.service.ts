import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IColor, NewColor } from '../color.model';

export type PartialUpdateColor = Partial<IColor> & Pick<IColor, 'id'>;

export type EntityResponseType = HttpResponse<IColor>;
export type EntityArrayResponseType = HttpResponse<IColor[]>;

@Injectable({ providedIn: 'root' })
export class ColorService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/colors');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(color: NewColor): Observable<EntityResponseType> {
    return this.http.post<IColor>(this.resourceUrl, color, { observe: 'response' });
  }

  update(color: IColor): Observable<EntityResponseType> {
    return this.http.put<IColor>(`${this.resourceUrl}/${this.getColorIdentifier(color)}`, color, { observe: 'response' });
  }

  partialUpdate(color: PartialUpdateColor): Observable<EntityResponseType> {
    return this.http.patch<IColor>(`${this.resourceUrl}/${this.getColorIdentifier(color)}`, color, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IColor>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IColor[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getColorIdentifier(color: Pick<IColor, 'id'>): number {
    return color.id;
  }

  compareColor(o1: Pick<IColor, 'id'> | null, o2: Pick<IColor, 'id'> | null): boolean {
    return o1 && o2 ? this.getColorIdentifier(o1) === this.getColorIdentifier(o2) : o1 === o2;
  }

  addColorToCollectionIfMissing<Type extends Pick<IColor, 'id'>>(
    colorCollection: Type[],
    ...colorsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const colors: Type[] = colorsToCheck.filter(isPresent);
    if (colors.length > 0) {
      const colorCollectionIdentifiers = colorCollection.map(colorItem => this.getColorIdentifier(colorItem)!);
      const colorsToAdd = colors.filter(colorItem => {
        const colorIdentifier = this.getColorIdentifier(colorItem);
        if (colorCollectionIdentifiers.includes(colorIdentifier)) {
          return false;
        }
        colorCollectionIdentifiers.push(colorIdentifier);
        return true;
      });
      return [...colorsToAdd, ...colorCollection];
    }
    return colorCollection;
  }
}
