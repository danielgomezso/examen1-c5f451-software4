import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CitaService } from '../../services/cita.service';
import { CrearCitaPayload } from '../../models/cita.model';

@Component({
  selector: 'app-cita-form',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './cita-form.html',
  styleUrl: './cita-form.css'
})
export class CitaFormComponent {
  private citaService = inject(CitaService);
  private router = inject(Router);

  cita: CrearCitaPayload = {
    pacienteId: 1,
    doctorId: 1,
    nivelPrioridad: 'MEDIA',
    motivoConsulta: '',
    montoConsulta: 15000
  };

  mensaje = '';
  guardando = false;

  crearCita(): void {
    if (
      !this.cita.pacienteId ||
      !this.cita.doctorId ||
      !this.cita.motivoConsulta.trim() ||
      this.cita.montoConsulta <= 0
    ) {
      this.mensaje = 'Complete todos los campos correctamente.';
      return;
    }

    this.guardando = true;
    this.mensaje = '';

    this.citaService.crearCita(this.cita).subscribe({
      next: () => {
        this.guardando = false;
        this.router.navigate(['/citas']);
      },
      error: error => {
        this.guardando = false;
        this.mensaje = error.error?.detail || 'No se pudo registrar la cita.';
      }
    });
  }
}