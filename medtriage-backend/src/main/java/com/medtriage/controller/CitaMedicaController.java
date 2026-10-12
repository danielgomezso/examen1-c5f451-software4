package com.medtriage.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.medtriage.dto.CitaMedicaDTO;
import com.medtriage.dto.CrearCitaDTO;
import com.medtriage.service.CitaMedicaService;

@RestController
@RequestMapping("/api/v1/citas")
@CrossOrigin(origins = "http://localhost:4200")
public class CitaMedicaController {

    private final CitaMedicaService citaService;

    public CitaMedicaController(CitaMedicaService citaService) {
        this.citaService = citaService;
    }

    @GetMapping
    public List<CitaMedicaDTO> obtenerTodas() {
        return citaService.obtenerTodasCitas();
    }

    @GetMapping("/prioridad/{prioridad}")
    public List<CitaMedicaDTO> obtenerPorPrioridad(
            @PathVariable String prioridad) {
        return citaService.obtenerPorPrioridad(prioridad);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CitaMedicaDTO crearCita(@RequestBody CrearCitaDTO dto) {
        return citaService.crearCita(dto);
    }

    @PatchMapping("/{id}/estado")
    public CitaMedicaDTO actualizarEstado(
            @PathVariable Long id,
            @RequestBody EstadoCitaRequest request) {
        return citaService.cambiarEstado(id, request.estado());
    }

    public record EstadoCitaRequest(String estado) {
    }
}