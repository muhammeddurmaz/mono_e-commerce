import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUserCart } from '../user-cart.model';
import { UserCartService } from '../service/user-cart.service';

@Injectable({ providedIn: 'root' })
export class UserCartRoutingResolveService implements Resolve<IUserCart | null> {
  constructor(protected service: UserCartService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUserCart | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((userCart: HttpResponse<IUserCart>) => {
          if (userCart.body) {
            return of(userCart.body);
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
