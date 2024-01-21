package br.com.processadoras.domain.repository.cache;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Context;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Inject;
import java.time.Duration;

@Context
public class RedisClientBuilder {

    @Value("${redis.uri}") String uri;

    @Bean
    @Inject
    public StatefulRedisConnection<String, String> connection() {
        RedisClient redisClient = RedisClient.create(uri);
        return redisClient.connect();
    }

    @Bean
    @Inject
    public RedisClient redisClient = RedisClient.
            create(RedisURI.builder().
                    withHost("localhost").
                    withPort(6379).
                    build());
}
