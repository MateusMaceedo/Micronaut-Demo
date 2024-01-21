package br.com.processadoras.domain.exceptions;

public class RejectedExecution extends RuntimeException {
    public RejectedExecution(String message) {
        super(message);
    }

    public RejectedExecution(String message, Throwable cause) {
        super(message, cause);
    }
}
