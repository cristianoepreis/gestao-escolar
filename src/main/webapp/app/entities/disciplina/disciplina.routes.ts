import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DisciplinaResolve from './route/disciplina-routing-resolve.service';

const disciplinaRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/disciplina.component').then(m => m.DisciplinaComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/disciplina-detail.component').then(m => m.DisciplinaDetailComponent),
    resolve: {
      disciplina: DisciplinaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/disciplina-update.component').then(m => m.DisciplinaUpdateComponent),
    resolve: {
      disciplina: DisciplinaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/disciplina-update.component').then(m => m.DisciplinaUpdateComponent),
    resolve: {
      disciplina: DisciplinaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default disciplinaRoute;
