import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'gestaoEscolarApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'aluno',
    data: { pageTitle: 'gestaoEscolarApp.aluno.home.title' },
    loadChildren: () => import('./aluno/aluno.routes'),
  },
  {
    path: 'professor',
    data: { pageTitle: 'gestaoEscolarApp.professor.home.title' },
    loadChildren: () => import('./professor/professor.routes'),
  },
  {
    path: 'disciplina',
    data: { pageTitle: 'gestaoEscolarApp.disciplina.home.title' },
    loadChildren: () => import('./disciplina/disciplina.routes'),
  },
  {
    path: 'curso',
    data: { pageTitle: 'gestaoEscolarApp.curso.home.title' },
    loadChildren: () => import('./curso/curso.routes'),
  },
  {
    path: 'nota',
    data: { pageTitle: 'gestaoEscolarApp.nota.home.title' },
    loadChildren: () => import('./nota/nota.routes'),
  },
  {
    path: 'matricula',
    data: { pageTitle: 'gestaoEscolarApp.matricula.home.title' },
    loadChildren: () => import('./matricula/matricula.routes'),
  },
  {
    path: 'ementa',
    data: { pageTitle: 'gestaoEscolarApp.ementa.home.title' },
    loadChildren: () => import('./ementa/ementa.routes'),
  },
  {
    path: 'administrador',
    data: { pageTitle: 'gestaoEscolarApp.administrador.home.title' },
    loadChildren: () => import('./administrador/administrador.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
