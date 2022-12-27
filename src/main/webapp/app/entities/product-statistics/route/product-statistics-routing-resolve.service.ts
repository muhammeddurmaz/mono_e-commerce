import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProductStatistics } from '../product-statistics.model';
import { ProductStatisticsService } from '../service/product-statistics.service';

@Injectable({ providedIn: 'root' })
export class ProductStatisticsRoutingResolveService implements Resolve<IProductStatistics | null> {
  constructor(protected service: ProductStatisticsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProductStatistics | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((productStatistics: HttpResponse<IProductStatistics>) => {
          if (productStatistics.body) {
            return of(productStatistics.body);
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
