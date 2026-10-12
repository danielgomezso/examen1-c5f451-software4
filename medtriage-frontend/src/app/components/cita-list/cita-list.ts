import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CitaService } from '../../services/cita.service';
import { CitaMedica } from '../../models/cita.model';

@Component({
  selector: 'app-cita-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './cita-list.html',
  styleUrl: './cita-list.css'
})
export class CitaListComponent implements OnInit {
  private citaService = inject(CitaService);

  citas = signal<CitaMedica[]>([]);
  filtroPrioridad = signal('TODAS');

  citasFiltradas = computed(() => {
    const prioridad = this.filtroPrioridad();

    if (prioridad === 'TODAS') {
      return this.citas();
    }

    return this.citas().filter(cita => cita.nivelPrioridad === prioridad);
  });

  ngOnInit(): void {
    this.cargarCitas();
  }

  cargarCitas(): void {
    this.citaService.getCitas().subscribe({
      next: citas => this.citas.set(citas),
      error: error => console.error('Error al cargar las citas', error)
    });
  }

  cambiarFiltro(prioridad: string): void {
    this.filtroPrioridad.set(prioridad);
  }

  avanzarEstado(cita: CitaMedica): void {
    const estados: Record<string, string> = {
      PENDIENTE: 'EN_ATENCION',
      EN_ATENCION: 'ATENDIDA'
    };

    const nuevoEstado = estados[cita.estado];

    if (!nuevoEstado) {
      return;
    }

    this.citaService.actualizarEstado(cita.id, nuevoEstado).subscribe({
      next: () => this.cargarCitas(),
      error: error => console.error('Error al actualizar el estado', error)
    });
  }
}