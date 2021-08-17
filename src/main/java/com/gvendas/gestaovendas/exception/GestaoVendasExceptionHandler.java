package com.gvendas.gestaovendas.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ControllerAdvice
public class GestaoVendasExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String CONST_VALIDATION_NOT_BLANK = "NotBlank";
    public static final String CONST_VALIDATION_NOT_NULL = "NotNull";
    public static final String CONST_VALIDATION_LENGTH = "Length";
    public static final String CONST_VALIDATION_PATTERN = "Pattern";
    public static final String CONST_VALIDATION_MIN = "Min";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<Error> errors = gerarListError(ex.getBindingResult());
        return handleExceptionInternal(ex, errors, headers, HttpStatus.BAD_REQUEST, request);
    }

    //indica para o ControllerAdvice que é um tratamento de exceção
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex, WebRequest request) {
        String msgUsuario = "Recurso não encontrado";
        String msgDev = ex.toString();
        List<Error> errors = Arrays.asList(new Error(msgUsuario, msgDev));
        return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        String msgUsuario = "Recurso não encontrado";
        String msgDev = ex.toString();
        List<Error> errors = Arrays.asList(new Error(msgUsuario, msgDev));
        return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<Object> handleRegraNegocioException(RegraNegocioException ex, WebRequest request) {
        String msgUsuario = ex.getMessage();
        String msgDev = ex.getMessage();
        List<Error> errors = Arrays.asList(new Error(msgUsuario, msgDev));
        return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    private List<Error> gerarListError(BindingResult bindingResult) {
        List<Error> errors = new ArrayList<Error>();

        bindingResult.getFieldErrors().forEach(fieldError -> {
            String msgUsuario = tratarMensagemErroParaUsuario(fieldError);
            String msgDev = fieldError.toString();
            errors.add(new Error(msgUsuario, msgDev));
        });
        return errors;
    }

    private String tratarMensagemErroParaUsuario(FieldError fieldError) {
        if (CONST_VALIDATION_NOT_BLANK.equals(fieldError.getCode())) {
            return fieldError.getDefaultMessage().concat(" é obrigatório");
        }
        if (CONST_VALIDATION_NOT_NULL.equals(fieldError.getCode())) {
            return fieldError.getDefaultMessage().concat(" é obrigatório");
        }
        if (CONST_VALIDATION_LENGTH.equals(fieldError.getCode())) {
            //Length retorna um array, [2] = min, [1] = min
            return fieldError.getDefaultMessage().concat(String.format(" deve ter entre %s e %s caracteres",
                    fieldError.getArguments()[2], fieldError.getArguments()[1]));
        }
        if (CONST_VALIDATION_PATTERN.equals(fieldError.getCode())) {
            return fieldError.getDefaultMessage().concat(" está com um formato inválido");
        }
        if (CONST_VALIDATION_MIN.equals(fieldError.getCode())) {
            return fieldError.getDefaultMessage().concat(String.format(" deve ser maior ou igual a %s",
                    fieldError.getArguments()[1]));
        }
        return fieldError.toString();
    }
}
