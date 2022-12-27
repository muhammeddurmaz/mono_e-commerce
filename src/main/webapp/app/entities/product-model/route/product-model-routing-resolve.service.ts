import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProductModel } from '../product-model.model';
import { ProductModelService } from '../service/product-model.service';

@Injectable({ providedIn: 'root' })
export class ProductModelRoutingResolveService implements Resolve<IProductModel | null> {
  constructor(protected service: ProductModelService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProductModel | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((productModel: HttpResponse<IProductModel>) => {
          if (productModel.body) {
            return of(productModel.body);
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
