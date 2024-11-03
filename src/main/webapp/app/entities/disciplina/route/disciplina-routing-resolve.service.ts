import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDisciplina } from '../disciplina.model';
import { DisciplinaService } from '../service/disciplina.service';

const disciplinaResolve = (route: ActivatedRouteSnapshot): Observable<null | IDisciplina> => {
  const id = route.params.id;
  if (id) {
    return inject(DisciplinaService)
      .find(id)
      .pipe(
        mergeMap((disciplina: HttpResponse<IDisciplina>) => {
          if (disciplina.body) {
            return of(disciplina.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default disciplinaResolve;
