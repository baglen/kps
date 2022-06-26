package com.kps.backend.exception;

public class RestrictedException extends RuntimeException{
    public RestrictedException(String message) {
        super(message);
    }
}
