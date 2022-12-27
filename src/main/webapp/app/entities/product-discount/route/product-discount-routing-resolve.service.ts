import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProductDiscount } from '../product-discount.model';
import { ProductDiscountService } from '../service/product-discount.service';

@Injectable({ providedIn: 'root' })
export class ProductDiscountRoutingResolveService implements Resolve<IProductDiscount | null> {
  constructor(protected service: ProductDiscountService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProductDiscount | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((productDiscount: HttpResponse<IProductDiscount>) => {
          if (productDiscount.body) {
            return of(productDiscount.body);
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
