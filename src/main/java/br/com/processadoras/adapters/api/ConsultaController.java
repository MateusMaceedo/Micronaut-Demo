package br.com.processadoras.adapters.api;

import br.com.processadoras.domain.dto.ProcessadorasFGTSDto;
import br.com.processadoras.domain.usecases.RealizaConsultaCEPUseCase;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

@Controller("/consulta")
public class ConsultaController {

    private final RealizaConsultaCEPUseCase useCase;

    public ConsultaController(RealizaConsultaCEPUseCase useCase) {
        this.useCase = useCase;
    }

    @Get("/{cep}")
    @ExecuteOn(TaskExecutors.IO)
    @Produces(MediaType.APPLICATION_JSON)
    public ProcessadorasFGTSDto consultaEndereco(String cep) throws Exception {
        return useCase.consultaCEP(cep);
    }
}
