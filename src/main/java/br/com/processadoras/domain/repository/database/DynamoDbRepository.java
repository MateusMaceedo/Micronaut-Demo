package br.com.processadoras.domain.repository.database;

import br.com.processadoras.domain.dto.ProcessadorasFGTSDto;
import br.com.processadoras.domain.usecases.RealizaConsultaCEPUseCase;
import jakarta.inject.Singleton;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.utils.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Singleton
public class DynamoDbRepository implements RepositoryData {
    private final DynamoDbClient dynamoDbClient;
    private static final String DYNAMO_TABLE = "tbt-consulta-cep";
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(DynamoDbRepository.class);


    public DynamoDbRepository(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    public void inserirItem(String key, ProcessadorasFGTSDto dto) {
        Map<String, AttributeValue> itemValues = new HashMap<>();
        itemValues.put("cep", AttributeValue.builder().s(dto.getCep()).build());
        itemValues.put("logradouro", AttributeValue.builder().s(dto.getLogradouro()).build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName(DYNAMO_TABLE)
                .item(itemValues)
                .build();

        dynamoDbClient.putItem(request);
    }

    public ProcessadorasFGTSDto getItem(String key) {
        return new ProcessadorasFGTSDto();
    }
}
