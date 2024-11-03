import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MatriculaResolve from './route/matricula-routing-resolve.service';

const matriculaRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/matricula.component').then(m => m.MatriculaComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/matricula-detail.component').then(m => m.MatriculaDetailComponent),
    resolve: {
      matricula: MatriculaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/matricula-update.component').then(m => m.MatriculaUpdateComponent),
    resolve: {
      matricula: MatriculaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/matricula-update.component').then(m => m.MatriculaUpdateComponent),
    resolve: {
      matricula: MatriculaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default matriculaRoute;
