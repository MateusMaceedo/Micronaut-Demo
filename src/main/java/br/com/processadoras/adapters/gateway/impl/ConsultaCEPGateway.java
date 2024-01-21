package br.com.processadoras.adapters.gateway.impl;

import br.com.processadoras.adapters.gateway.IConsultaCEPGateway;
import br.com.processadoras.domain.dto.ProcessadorasFGTSDto;
import io.micronaut.context.annotation.Property;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import jakarta.inject.Singleton;

@Singleton
public class ConsultaCEPGateway implements IConsultaCEPGateway {

    private final HttpClient httpClient;

    public ConsultaCEPGateway(@Client("https://viacep.com.br") HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public ProcessadorasFGTSDto consultarCEP(String cep) {
        HttpRequest<?> request = HttpRequest.GET("/ws/" + cep + "/json");
        HttpResponse<ProcessadorasFGTSDto> response = httpClient.toBlocking().exchange(request, ProcessadorasFGTSDto.class);
        return response.body();
    }
}
