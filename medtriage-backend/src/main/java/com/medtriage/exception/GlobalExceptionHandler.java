package com.medtriage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ProblemDetail manejarNoEncontrado(RecursoNoEncontradoException exception) {
        ProblemDetail detalle = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        detalle.setTitle("Recurso no existe");
        return detalle;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail manejarArgumentoInvalido(IllegalArgumentException exception) {
        ProblemDetail detalle = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        detalle.setTitle("Solicitud invalida");
        return detalle;
    }

    @ExceptionHandler(RuntimeException.class)
    public ProblemDetail manejarError(RuntimeException exception) {
        ProblemDetail detalle = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        detalle.setTitle("Error de la solicitud");
        return detalle;
    }
}