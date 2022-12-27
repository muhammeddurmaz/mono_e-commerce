import { Routes } from '@angular/router';

import { ErrorComponent } from './error.component';

export const errorRoute: Routes = [
  {
    path: 'error',
    component: ErrorComponent,
    data: {
      pageTitle: 'Hata Sayfası!',
    },
  },
  {
    path: 'accessdenied',
    component: ErrorComponent,
    data: {
      pageTitle: 'Hata Sayfası!',
      errorMessage: 'Bu sayfaya erişmeye yetkiniz yok.',
    },
  },
  {
    path: '404',
    component: ErrorComponent,
    data: {
      pageTitle: 'Hata Sayfası!',
      errorMessage: 'Sayfa mevcut değil.',
    },
  },
  {
    path: '**',
    redirectTo: '/404',
  },
];
