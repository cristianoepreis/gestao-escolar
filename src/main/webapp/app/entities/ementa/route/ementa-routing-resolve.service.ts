import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEmenta } from '../ementa.model';
import { EmentaService } from '../service/ementa.service';

const ementaResolve = (route: ActivatedRouteSnapshot): Observable<null | IEmenta> => {
  const id = route.params.id;
  if (id) {
    return inject(EmentaService)
      .find(id)
      .pipe(
        mergeMap((ementa: HttpResponse<IEmenta>) => {
          if (ementa.body) {
            return of(ementa.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default ementaResolve;
