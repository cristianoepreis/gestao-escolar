import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import EmentaResolve from './route/ementa-routing-resolve.service';

const ementaRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/ementa.component').then(m => m.EmentaComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/ementa-detail.component').then(m => m.EmentaDetailComponent),
    resolve: {
      ementa: EmentaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/ementa-update.component').then(m => m.EmentaUpdateComponent),
    resolve: {
      ementa: EmentaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/ementa-update.component').then(m => m.EmentaUpdateComponent),
    resolve: {
      ementa: EmentaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default ementaRoute;
