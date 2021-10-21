package com.projectflow.projectflow.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String UNEXPECTED = "Unexpected Exception caused by: ";

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(final GlobalException exception) {
        final ErrorCode errorCode = exception.getErrorCode();
        return new ResponseEntity<>(new ErrorResponse(errorCode.getStatus(), errorCode.getMessage()),
                HttpStatus.valueOf(errorCode.getStatus()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> unexpectedException(final Exception exception) {
        return new ResponseEntity<>(new ErrorResponse(500, UNEXPECTED + exception.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
