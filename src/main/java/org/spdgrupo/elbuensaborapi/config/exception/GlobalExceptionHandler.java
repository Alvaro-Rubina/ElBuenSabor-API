package org.spdgrupo.elbuensaborapi.config.exception;

import com.auth0.exception.Auth0Exception;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                "INTERNAL_SERVER_ERROR",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                "INTERNAL_SERVER_ERROR",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

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

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String mensaje;
        if (ex.getRequiredType() != null && ex.getRequiredType().isEnum()) {
            String valoresValidos = Arrays.stream(ex.getRequiredType().getEnumConstants())
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));
            mensaje = "Valor inválido para " + ex.getName() + ": '" + ex.getValue() +
                    "'. Los valores permitidos son: [" + valoresValidos + "]";
        } else {
            mensaje = "Parámetro inválido: " + ex.getMessage();
        }
        ErrorResponse error = ErrorResponse.builder()
                .mensaje(mensaje)
                .codigo("INVALID_PARAM")
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(org.springframework.web.bind.MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameter(
            org.springframework.web.bind.MissingServletRequestParameterException ex) {
        String mensaje = "Falta el parámetro requerido: " + ex.getParameterName();
        ErrorResponse error = ErrorResponse.builder()
                .mensaje(mensaje)
                .codigo("MISSING_PARAM")
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

    @ExceptionHandler(Auth0Exception.class)
    public ResponseEntity<ErrorResponse> handleAuth0Exception(Auth0Exception ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                "AUTH0_ERROR",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
