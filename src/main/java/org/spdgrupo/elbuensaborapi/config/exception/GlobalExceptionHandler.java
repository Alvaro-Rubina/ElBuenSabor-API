package org.spdgrupo.elbuensaborapi.config.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidRolException.class)
    public ResponseEntity<String> handleInvalidRolException(InvalidRolException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CyclicParentException.class)
    public ResponseEntity<String> handleHierarchyCycleException(CyclicParentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        if (ex.getCause() instanceof InvalidFormatException) {
            InvalidFormatException cause = (InvalidFormatException) ex.getCause();
            if (cause.getTargetType() != null && cause.getTargetType().isEnum()) {
                String valoresValidos = Arrays.stream(cause.getTargetType().getEnumConstants())
                        .map(Object::toString)
                        .collect(Collectors.joining(", "));
                return new ResponseEntity<>(
                        "Valor inválido: '" + cause.getValue() + "'. Los valores permitidos son: [" + valoresValidos + "]",
                        HttpStatus.BAD_REQUEST
                );
            }
        }
        return new ResponseEntity<>("Error en el formato del JSON: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
