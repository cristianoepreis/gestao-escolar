import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AdministradorResolve from './route/administrador-routing-resolve.service';

const administradorRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/administrador.component').then(m => m.AdministradorComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/administrador-detail.component').then(m => m.AdministradorDetailComponent),
    resolve: {
      administrador: AdministradorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/administrador-update.component').then(m => m.AdministradorUpdateComponent),
    resolve: {
      administrador: AdministradorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/administrador-update.component').then(m => m.AdministradorUpdateComponent),
    resolve: {
      administrador: AdministradorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default administradorRoute;
