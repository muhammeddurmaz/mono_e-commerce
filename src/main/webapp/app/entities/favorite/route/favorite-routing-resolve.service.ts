import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFavorite } from '../favorite.model';
import { FavoriteService } from '../service/favorite.service';

@Injectable({ providedIn: 'root' })
export class FavoriteRoutingResolveService implements Resolve<IFavorite | null> {
  constructor(protected service: FavoriteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFavorite | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((favorite: HttpResponse<IFavorite>) => {
          if (favorite.body) {
            return of(favorite.body);
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
