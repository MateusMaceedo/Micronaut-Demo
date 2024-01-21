package br.com.processadoras.adapters.handler;

import br.com.processadoras.domain.exceptions.TooManyRequestsException;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

@Produces
@Singleton
@Requires(classes = {TooManyRequestsException.class, ExceptionHandler.class})
public class TooManyRequestsExceptionHandler implements ExceptionHandler<TooManyRequestsException, HttpResponse> {

    @Override
    public HttpResponse<?> handle(HttpRequest request, TooManyRequestsException exception) {
        return HttpResponse.<String>status(io.micronaut.http.HttpStatus.TOO_MANY_REQUESTS)
                .header("RATE-LIMIT", "Limite de requests atingidos")
                .body("{\"codigo\": \"RATE-LIMIT\", \"mensagem\": \"Limite de requisições atingidos, por favor tente mais tarde\"}");
    }
}
