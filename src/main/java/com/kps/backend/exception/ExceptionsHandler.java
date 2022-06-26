package com.kps.backend.exception;

import com.kps.backend.security.exception.AuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler({ObjectAlreadyExistsException.class, ObjectNotFoundException.class, BadRequestException.class})
    public ResponseEntity<Object> handleObjectExceptions(RuntimeException ex, WebRequest request) {
        return new ResponseEntity<>(bodyError(ex, HttpStatus.BAD_REQUEST, request), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<Object> handleAuthorizationException(AuthorizationException ex, WebRequest request) {
        return new ResponseEntity<>(bodyError(ex, HttpStatus.UNAUTHORIZED, request), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RestrictedException.class)
    public ResponseEntity<Object> handleRestrictedException(RuntimeException ex, WebRequest request) {
        return new ResponseEntity<>(bodyError(ex, HttpStatus.FORBIDDEN, request), HttpStatus.FORBIDDEN);
    }

    private Map<Object, Object> bodyError(Exception ex, HttpStatus status, WebRequest request){
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("timestamp", Instant.now().getEpochSecond());
        error.put("status", status.value());
        error.put("message", ex.getLocalizedMessage());
        error.put("path", ((ServletWebRequest)request).getRequest().getRequestURI());
        Map<Object, Object> body = new LinkedHashMap<>();
        body.put("success", "false");
        body.put("error", error);
        return body;
    }
}
