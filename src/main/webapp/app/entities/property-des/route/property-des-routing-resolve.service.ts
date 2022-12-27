import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPropertyDes } from '../property-des.model';
import { PropertyDesService } from '../service/property-des.service';

@Injectable({ providedIn: 'root' })
export class PropertyDesRoutingResolveService implements Resolve<IPropertyDes | null> {
  constructor(protected service: PropertyDesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPropertyDes | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((propertyDes: HttpResponse<IPropertyDes>) => {
          if (propertyDes.body) {
            return of(propertyDes.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
