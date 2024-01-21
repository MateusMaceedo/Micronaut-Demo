package br.com.processadoras.domain.repository.database.config;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import java.net.URI;

@Factory
public class DynamoDBFactory {

    @Value("${aws.endpoints.dynamodb.endpointOverride}") String uri;

    @Bean
    @Singleton
    public DynamoDbClient amazonDynamoDB() {
        return DynamoDbClient.builder()
                .endpointOverride(URI.create(uri))
                .region(Region.SA_EAST_1)
                .build();
    }
}
