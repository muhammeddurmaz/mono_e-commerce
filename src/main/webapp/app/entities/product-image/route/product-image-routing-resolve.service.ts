import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProductImage } from '../product-image.model';
import { ProductImageService } from '../service/product-image.service';

@Injectable({ providedIn: 'root' })
export class ProductImageRoutingResolveService implements Resolve<IProductImage | null> {
  constructor(protected service: ProductImageService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProductImage | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((productImage: HttpResponse<IProductImage>) => {
          if (productImage.body) {
            return of(productImage.body);
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
