package br.com.processadoras.domain.exceptions;

public class DeserializationException extends RuntimeException{
    public DeserializationException(String mensagem) {
        super(mensagem);
    }

    public DeserializationException(String mensagem, Throwable cause) {
        super(mensagem, cause);
    }
}
