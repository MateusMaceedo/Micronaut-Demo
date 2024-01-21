package br.com.processadoras.domain.usecases;

import br.com.processadoras.adapters.gateway.IConsultaCEPGateway;
import br.com.processadoras.domain.dto.ProcessadorasFGTSDto;
import br.com.processadoras.domain.exceptions.DeserializationException;
import br.com.processadoras.domain.exceptions.TooManyRequestsException;
import br.com.processadoras.domain.repository.cache.RedisClientBuilder;
import br.com.processadoras.domain.repository.database.RepositoryData;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.api.sync.RedisCommands;
import io.micronaut.core.serialize.exceptions.SerializationException;
import jakarta.inject.Singleton;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;
import java.util.function.Supplier;

@Singleton
public class RealizaConsultaCEPUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(RealizaConsultaCEPUseCase.class);
    private final IConsultaCEPGateway consultaCEPGateway;
    private final RedisCommands<String, String> redisCommands;
    private final ObjectMapper objectMapper;
    private Bucket bucket;
    private final RepositoryData repositoryData;
    private static String KEY_CEP = "ConsultaCEP";
    private static String LOG_TAG = "CacheBucket";
    private static final String BUCKET_KEY = "RateLimitBucket";

    public RealizaConsultaCEPUseCase(IConsultaCEPGateway consultaCEPGateway, RedisClientBuilder redisClientBuilder, RepositoryData repositoryData) {
        this.repositoryData = repositoryData;
        this.objectMapper = new ObjectMapper();
        this.consultaCEPGateway = consultaCEPGateway;
        this.redisCommands = redisClientBuilder.redisClient.connect().sync();
        salvaEstadoBucket();
    }

    private void salvaEstadoBucket() {
        RedisClientBuilder redisClientBuilder = new RedisClientBuilder();
        LettuceBasedProxyManager proxyManager = LettuceBasedProxyManager.
                builderFor(redisClientBuilder.redisClient).
                withExpirationStrategy(
                        ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(Duration.ofSeconds(100))
                ).
                build();

        proxyManager.builder().build(BUCKET_KEY.getBytes(),   configuracaoBucket());
    }

    public Supplier<BucketConfiguration> configuracaoBucket() {
        BucketConfiguration configuration = BucketConfiguration.builder()
                .addLimit(Bandwidth.classic(10, Refill.intervally(10, Duration.ofMinutes(1))))
                .build();
        return null;
    }

    public ProcessadorasFGTSDto consultaCEP(String cep) throws Exception {
        String cachedCEP = redisCommands.get(KEY_CEP + cep);

        if (!tryConsume())
            throw new TooManyRequestsException("Request limit atingida");

        if (cachedCEP != null) {
            return deserialize(cachedCEP, ProcessadorasFGTSDto.class);
        } else {
            try {
                var cepRetorno = consultaCEPGateway.consultarCEP(cep);
                redisCommands.set(KEY_CEP + cep, serialize(cepRetorno));
                repositoryData.inserirItem(cep, cepRetorno);
                return cepRetorno;
            } catch (Exception e) {
                throw new Exception("Ocorreu um erro ao realizar a operação, analise os logs", e);
            }
        }
    }

    private boolean tryConsume() {
        //boolean response = bucket.tryConsume(1);
        boolean probe = bucket.tryConsumeAndReturnRemaining(1).isConsumed();

        if (!probe) {
            LOGGER.info("{}: Rate limit atingido", LOG_TAG);
        }

        return probe;
    }

    private String serialize(Object object) throws JsonProcessingException {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Erro ao serializar objeto", e);
        }
    }

    private <T> T deserialize(String content, Class<T> valueType) {
        try {
            return objectMapper.readValue(content, valueType);
        } catch (JsonProcessingException e) {
            throw new DeserializationException("Error ao deserializar content", e);
        }
    }
}
