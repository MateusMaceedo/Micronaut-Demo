package br.com.processadoras.domain.repository.database;

import br.com.processadoras.domain.dto.ProcessadorasFGTSDto;

public interface RepositoryData {
    void inserirItem(String key, ProcessadorasFGTSDto dto);
}
