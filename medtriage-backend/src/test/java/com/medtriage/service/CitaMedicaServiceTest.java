package com.medtriage.service;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.medtriage.model.CitaMedica;
import com.medtriage.model.Doctor;
import com.medtriage.model.Paciente;
import com.medtriage.repository.CitaMedicaRepository;
import com.medtriage.repository.DoctorRepository;
import com.medtriage.repository.PacienteRepository;

@ExtendWith(MockitoExtension.class)
class CitaMedicaServiceTest {

    @Mock
    private CitaMedicaRepository citaRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private PacienteRepository pacienteRepository;

    @InjectMocks
    private CitaMedicaService citaService;

    @Test
    void crearCitaConDoctorDisponible() {
        Paciente paciente = new Paciente();
        paciente.setId(1L);
        paciente.setNombreCompleto("Daniel Gomez Solano");

        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setNombre("Jonathan Castro");
        doctor.setDisponible(true);

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(citaRepository.findByCodigoCita(any())).thenReturn(Optional.empty());
        when(citaRepository.save(any(CitaMedica.class))).thenAnswer(invocation -> {
            CitaMedica cita = invocation.getArgument(0);
            cita.setId(1L);
            return cita;
        });

        com.medtriage.dto.CrearCitaDTO dto = new com.medtriage.dto.CrearCitaDTO();
        dto.setPacienteId(1L);
        dto.setDoctorId(1L);
        dto.setNivelPrioridad("ALTA");
        dto.setMotivoConsulta("Gripe");
        dto.setMontoConsulta(new BigDecimal("30000.00"));

        var resultado = citaService.crearCita(dto);

        assertEquals("PENDIENTE", resultado.getEstado());
        assertEquals("ALTA", resultado.getNivelPrioridad());
        assertEquals("Daniel Gomez Solano", resultado.getNombrePaciente());
        verify(citaRepository).save(any(CitaMedica.class));
    }

    @Test
    void crearCitaConDoctorNoDisponible() {
        Paciente paciente = new Paciente();
        paciente.setId(1L);

        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setDisponible(false);

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        com.medtriage.dto.CrearCitaDTO dto = new com.medtriage.dto.CrearCitaDTO();
        dto.setPacienteId(1L);
        dto.setDoctorId(1L);
        dto.setNivelPrioridad("ALTA");
        dto.setMotivoConsulta("Fiebre alta");
        dto.setMontoConsulta(new BigDecimal("25000.00"));

        assertThrows(RuntimeException.class, () -> citaService.crearCita(dto));
        verify(citaRepository, never()).save(any(CitaMedica.class));
    }
}