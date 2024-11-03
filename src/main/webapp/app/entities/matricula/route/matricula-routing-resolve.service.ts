import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMatricula } from '../matricula.model';
import { MatriculaService } from '../service/matricula.service';

const matriculaResolve = (route: ActivatedRouteSnapshot): Observable<null | IMatricula> => {
  const id = route.params.id;
  if (id) {
    return inject(MatriculaService)
      .find(id)
      .pipe(
        mergeMap((matricula: HttpResponse<IMatricula>) => {
          if (matricula.body) {
            return of(matricula.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default matriculaResolve;
