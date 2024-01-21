package br.com.processadoras.domain.exceptions;

public class TooManyRequestsException extends RuntimeException {
    private final int statusCode;

    public TooManyRequestsException(String message) {
        super(message);
        this.statusCode = 429;
    }

    public TooManyRequestsException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = 429;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
