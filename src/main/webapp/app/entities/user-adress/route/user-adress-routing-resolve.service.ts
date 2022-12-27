import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUserAdress } from '../user-adress.model';
import { UserAdressService } from '../service/user-adress.service';

@Injectable({ providedIn: 'root' })
export class UserAdressRoutingResolveService implements Resolve<IUserAdress | null> {
  constructor(protected service: UserAdressService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUserAdress | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((userAdress: HttpResponse<IUserAdress>) => {
          if (userAdress.body) {
            return of(userAdress.body);
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
