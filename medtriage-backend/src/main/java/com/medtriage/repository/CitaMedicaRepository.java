package com.medtriage.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medtriage.model.CitaMedica;

public interface CitaMedicaRepository extends JpaRepository<CitaMedica, Long> {
    List<CitaMedica> findAllByOrderByFechaCitaDesc();

    List<CitaMedica> findByNivelPrioridadOrderByFechaCitaDesc(String nivelPrioridad);

    Optional<CitaMedica> findByCodigoCita(String codigoCita);
}