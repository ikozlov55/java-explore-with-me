package ru.practicum.ewm.core.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorHandler {

    public static final String BAD_REQUEST_REASON = "Incorrectly made request";
    public static final String NOT_FOUND_REASON = "The required object was not found";
    public static final String DATA_INTEGRITY_VIOLATION_REASON = "Integrity constraint has been violated";
    public static final String FORBIDDEN_REASON = "For the requested operation the conditions are not met.";
    public static final String SERVER_ERROR_REASON = "Something went wrong";

    @ExceptionHandler({
            ValidationException.class,
            MethodArgumentTypeMismatchException.class,
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(Exception e) {
        String message = switch (e) {
            case MethodArgumentNotValidException typeEx -> typeEx
                    .getBindingResult().getFieldErrors().stream()
                    .map(this::errorFormat)
                    .collect(Collectors.joining("\n"));
            default -> e.getMessage();
        };
        return new ErrorResponse(HttpStatus.BAD_REQUEST.name(),
                BAD_REQUEST_REASON,
                message,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(NotFoundException e) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.name(),
                NOT_FOUND_REASON,
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDataIntegrityViolation(DataIntegrityViolationException e) {
        return new ErrorResponse(HttpStatus.CONFLICT.name(),
                DATA_INTEGRITY_VIOLATION_REASON,
                e.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler({
            ForbiddenException.class,
            ConflictException.class
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleForbidden(Exception e) {
        return new ErrorResponse(HttpStatus.FORBIDDEN.name(),
                FORBIDDEN_REASON,
                e.getMessage(),
                LocalDateTime.now()
        );
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalError(Throwable e) {
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.name(),
                SERVER_ERROR_REASON,
                e.getMessage(),
                LocalDateTime.now()
        );
    }


    private String errorFormat(FieldError error) {
        return String.format("Field: %s. Error: %s. Value: %s", error.getField(),
                error.getDefaultMessage(), error.getRejectedValue());
    }
}
