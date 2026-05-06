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

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandlerController {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleArgumentNotValidException(MethodArgumentNotValidException ex) {
        String detailedMessage = ex.getFieldErrors().stream()
                .map(this::createArgumentErrorMessage)
                .collect(Collectors.joining(", "));

        log.warn("Validation failed: {}", detailedMessage);

        return buildResponse(
                "Validation failed: " + detailedMessage,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDto> handleArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        String parameterName = ex.getName();
        String requiredType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown";
        Object providedValue = ex.getValue();

        log.warn("Parameter '{}' type mismatch. Excepted type: '{}'. Provided value: '{}'",
                parameterName, requiredType, providedValue);

        return buildResponse(
                "The parameter '%s' must be of type '%s'".formatted(parameterName, requiredType),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({InvalidJwtException.class, JwtClaimsException.class, BadCredentialsException.class})
    public ResponseEntity<ErrorDto> handleAuthenticationException(Exception ex) {
        log.warn(ex.getMessage());
        return buildResponse("Invalid or expired token", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({UserNotFoundException.class, TaskNotFoundException.class})
    public ResponseEntity<ErrorDto> handleNotFoundException(Exception ex) {
        log.warn(ex.getMessage());
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ErrorDto> handleConflictException(UserAlreadyExistException ex) {
        log.warn(ex.getMessage());
        return buildResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(JwtGenerationException.class)
    public ResponseEntity<ErrorDto> handleJwtGenerationException(JwtGenerationException ex) {
        log.error("Error generating token", ex);
        return buildResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleInternalServerException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return buildResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorDto> buildResponse(String message, HttpStatus status) {
        ErrorDto error = new ErrorDto(message);
        return ResponseEntity.status(status).body(error);
    }

    private String createArgumentErrorMessage(FieldError field) {
        final String DEFAULT_ERROR_TEXT = "Invalid argument value";

        String fieldName = field.getField();
        String message = field.getDefaultMessage() != null ? field.getDefaultMessage() : DEFAULT_ERROR_TEXT;
        return "%s: %s".formatted(fieldName, message);
    }
}
