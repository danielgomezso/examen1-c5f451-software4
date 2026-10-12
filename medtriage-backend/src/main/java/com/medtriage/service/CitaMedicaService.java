package com.medtriage.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.medtriage.dto.CitaMedicaDTO;
import com.medtriage.dto.CrearCitaDTO;
import com.medtriage.model.CitaMedica;
import com.medtriage.model.Doctor;
import com.medtriage.model.Paciente;
import com.medtriage.repository.CitaMedicaRepository;
import com.medtriage.repository.DoctorRepository;
import com.medtriage.repository.PacienteRepository;

@Service
public class CitaMedicaService {
    private final CitaMedicaRepository citaMedicaRepository;
    private final DoctorRepository doctorRepository;
    private final PacienteRepository pacienteRepository;

    public CitaMedicaService(CitaMedicaRepository citaRepository, DoctorRepository doctorRepository,
            PacienteRepository pacienteRepository) {
        this.citaMedicaRepository = citaRepository;
        this.doctorRepository = doctorRepository;
        this.pacienteRepository = pacienteRepository;
    }

    public List<CitaMedicaDTO> obtenerTodasCitas() {
        return citaMedicaRepository.findAllByOrderByFechaCitaDesc().stream().map(this::transformarDTO).toList();
    }

    public List<CitaMedicaDTO> obtenerPorPrioridad(String prioridad) {
        return citaMedicaRepository.findByNivelPrioridadOrderByFechaCitaDesc(prioridad.toUpperCase()).stream().map(this::transformarDTO).toList();
    }

    public CitaMedicaDTO crearCita(CrearCitaDTO dto) {
        String codigo;
        Paciente paciente = pacienteRepository.findById(dto.getPacienteId()).orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        Doctor doctor = doctorRepository.findById(dto.getDoctorId()).orElseThrow(() -> new RuntimeException("Doctor no encontrado"));

        if (!Boolean.TRUE.equals(doctor.getDisponible())) {
            throw new RuntimeException("Doctor no disponible");
        }

        do {
            codigo = "CIT-2026-" + String.format("%04d",(int) (Math.random() * 10000));
        } while (citaMedicaRepository.findByCodigoCita(codigo).isPresent());

        CitaMedica cita = new CitaMedica();
        cita.setCodigoCita(codigo);
        cita.setPaciente(paciente);
        cita.setDoctor(doctor);
        cita.setNivelPrioridad(dto.getNivelPrioridad().toUpperCase());
        cita.setMotivoConsulta(dto.getMotivoConsulta());
        cita.setEstado("PENDIENTE");
        cita.setMontoConsulta(dto.getMontoConsulta());
        cita.setFechaCita(LocalDateTime.now());

        return transformarDTO(citaMedicaRepository.save(cita));
    }

    public CitaMedicaDTO cambiarEstado(Long id, String nuevoEstado) {
        CitaMedica cita = citaMedicaRepository.findById(id).orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        String estado = nuevoEstado.toUpperCase();

        if (!List.of("EN_ATENCION", "ATENDIDA", "CANCELADA").contains(estado)) {
            throw new RuntimeException("Estado invalido");
        }

        cita.setEstado(estado);
        return transformarDTO(citaMedicaRepository.save(cita));
    }

    private CitaMedicaDTO transformarDTO(CitaMedica cita) {
        CitaMedicaDTO dto = new CitaMedicaDTO();
        dto.setId(cita.getId());
        dto.setCodigoCita(cita.getCodigoCita());
        dto.setNombrePaciente(cita.getPaciente().getNombreCompleto());
        dto.setNombreDoctor(cita.getDoctor().getNombre());
        dto.setNivelPrioridad(cita.getNivelPrioridad());
        dto.setMotivoConsulta(cita.getMotivoConsulta());
        dto.setEstado(cita.getEstado());
        dto.setMontoConsulta(cita.getMontoConsulta());
        dto.setFechaCita(cita.getFechaCita());
        return dto;
    }
}