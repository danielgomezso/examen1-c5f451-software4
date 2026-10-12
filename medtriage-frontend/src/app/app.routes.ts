import { Routes } from '@angular/router';
import { CitaListComponent } from './components/cita-list/cita-list';
import { CitaFormComponent } from './components/cita-form/cita-form';

export const routes: Routes = [
  { path: '', redirectTo: 'citas', pathMatch: 'full' },
  { path: 'citas', component: CitaListComponent },
  { path: 'nueva-cita', component: CitaFormComponent },
  { path: '**', redirectTo: 'citas' }
];