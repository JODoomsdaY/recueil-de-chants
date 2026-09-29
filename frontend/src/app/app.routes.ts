import { Routes } from '@angular/router';

import { ChantDetail } from './chants/chant-detail';
import { ChantForm } from './chants/chant-form';
import { ChantList } from './chants/chant-list';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'chants' },
  { path: 'chants', component: ChantList, title: 'Recueil de chants' },
  { path: 'chants/nouveau', component: ChantForm, title: 'Nouveau chant' },
  { path: 'chants/:id', component: ChantDetail, title: 'Chant' },
  { path: 'chants/:id/modifier', component: ChantForm, title: 'Modifier le chant' },
  { path: '**', redirectTo: 'chants' },
];
