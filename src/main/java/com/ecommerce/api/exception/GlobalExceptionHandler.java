package com.ecommerce.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<Map<String, String>> handleRegraNegocioException(RegraNegocioException ex) {
        Map<String, String> erro = new HashMap<>();
        erro.put("erro", ex.getMessage());
        return ResponseEntity.badRequest().body(erro);
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<Map<String, String>> handleRecursoNaoEncontradoException(RecursoNaoEncontradoException ex) {
        Map<String, String> erro = new HashMap<>();
        erro.put("erro", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> erros = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            erros.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(erros);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenerics(Exception ex) {
        Map<String, String> erro = new HashMap<>();
        erro.put("erro", "Ocorreu um erro inesperado. " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }
}
