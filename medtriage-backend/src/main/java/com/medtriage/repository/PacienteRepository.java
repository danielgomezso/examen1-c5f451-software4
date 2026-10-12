package com.medtriage.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medtriage.model.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findByIdentificacion(String identificacion);
}