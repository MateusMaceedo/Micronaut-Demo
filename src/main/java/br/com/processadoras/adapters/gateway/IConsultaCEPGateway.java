package br.com.processadoras.adapters.gateway;

import br.com.processadoras.domain.dto.ProcessadorasFGTSDto;

public interface IConsultaCEPGateway {
    ProcessadorasFGTSDto consultarCEP(String cep);
}
