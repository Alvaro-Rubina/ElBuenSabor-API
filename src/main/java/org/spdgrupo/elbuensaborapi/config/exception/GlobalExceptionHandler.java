package org.spdgrupo.elbuensaborapi.config.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                "NOT_FOUND",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidRolException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRolException(InvalidRolException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                "INVALID_ROL",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(CyclicParentException.class)
    public ResponseEntity<ErrorResponse> handleHierarchyCycleException(CyclicParentException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                "CYCLIC_PARENT",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                "ILLEGAL_ARGUMENT",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String mensaje;
        if (ex.getCause() instanceof InvalidFormatException) {
            InvalidFormatException cause = (InvalidFormatException) ex.getCause();
            if (cause.getTargetType() != null && cause.getTargetType().isEnum()) {
                String valoresValidos = Arrays.stream(cause.getTargetType().getEnumConstants())
                        .map(Object::toString)
                        .collect(Collectors.joining(", "));
                String nombreCampo = cause.getTargetType().getSimpleName();
                mensaje = nombreCampo + " inválido: '" + cause.getValue() +
                        "'. Los valores permitidos son: [" + valoresValidos + "]";
            } else {
                mensaje = "Error en el formato del JSON: " + ex.getMessage();
            }
        } else {
            mensaje = "Error en el formato del JSON: " + ex.getMessage();
        }

        ErrorResponse error = ErrorResponse.builder()
                .mensaje(mensaje)
                .codigo("INVALID_FORMAT")
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MPException.class)
    public ResponseEntity<ErrorResponse> handleMPException(MPException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                "MP_ERROR",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(MPApiException.class)
    public ResponseEntity<ErrorResponse> handleMPApiException(MPApiException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                "MP_API_ERROR",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
