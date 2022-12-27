import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISellerStatistics } from '../seller-statistics.model';
import { SellerStatisticsService } from '../service/seller-statistics.service';

@Injectable({ providedIn: 'root' })
export class SellerStatisticsRoutingResolveService implements Resolve<ISellerStatistics | null> {
  constructor(protected service: SellerStatisticsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISellerStatistics | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sellerStatistics: HttpResponse<ISellerStatistics>) => {
          if (sellerStatistics.body) {
            return of(sellerStatistics.body);
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
