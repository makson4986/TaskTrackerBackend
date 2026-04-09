package org.makson.tasktrackerbackend.controllers;

import lombok.extern.slf4j.Slf4j;
import org.makson.tasktrackerbackend.dto.ErrorDto;
import org.makson.tasktrackerbackend.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandlerController {
    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<?> handleBadRequestException(Exception e) {
        ErrorDto errorDto;
        final String DEFAULT_ERROR_TEXT = "Error! Please check the entered data for accuracy";

        if (e instanceof MethodArgumentNotValidException) {
            FieldError fieldError = ((MethodArgumentNotValidException) e).getFieldError();
            String textError = fieldError != null ? fieldError.getDefaultMessage() : DEFAULT_ERROR_TEXT;
            errorDto = new ErrorDto(textError);
        } else {
            errorDto = new ErrorDto(DEFAULT_ERROR_TEXT);
        }

        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler({
            InvalidJwtException.class,
            JwtClaimsException.class,
            JwtGenerationException.class,
            BadCredentialsException.class
    })
    public ResponseEntity<?> handleAuthenticationException(Exception ex) {
        log.warn(ex.getMessage());
        ErrorDto error = new ErrorDto("Bad credentials");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(Exception ex) {
        log.warn(ex.getMessage());
        ErrorDto error = new ErrorDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<?> handleConflictException(Exception ex) {
        log.warn(ex.getMessage());
        ErrorDto error = new ErrorDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleInternalServerException(Exception ex) {
        log.error(ex.getMessage(), ex);
        ErrorDto error = new ErrorDto("Internal server error");
        return ResponseEntity.internalServerError().body(error);
    }
}
