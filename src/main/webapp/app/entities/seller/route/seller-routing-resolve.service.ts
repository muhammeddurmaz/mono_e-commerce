import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISeller } from '../seller.model';
import { SellerService } from '../service/seller.service';

@Injectable({ providedIn: 'root' })
export class SellerRoutingResolveService implements Resolve<ISeller | null> {
  constructor(protected service: SellerService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISeller | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((seller: HttpResponse<ISeller>) => {
          if (seller.body) {
            return of(seller.body);
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
