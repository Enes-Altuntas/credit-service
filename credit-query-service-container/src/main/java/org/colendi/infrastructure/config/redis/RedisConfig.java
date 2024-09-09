package org.colendi.infrastructure.config.redis;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.ReadFrom;
import io.lettuce.core.SocketOptions;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

  private final Environment environment;

  private static final String STANDALONE_REDIS_HOST = "localhost";
  private static final Integer STANDALONE_REDIS_PORT = 6379;

  private LettuceClientConfiguration buildLettuceClientConfig(ReadFrom readFrom) {
    return LettuceClientConfiguration.builder()
        .readFrom(readFrom)
        .clientName("credit-query-service")
        .clientOptions(ClientOptions.builder()
            .autoReconnect(true)
            .disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS)
            .socketOptions(SocketOptions.builder()
                .connectTimeout(Duration.ofSeconds(20))
                .keepAlive(true)
                .build())
            .build())
        .build();
  }

  private RedisConnectionFactory standaloneFactory(int database) {
    LettuceClientConfiguration clientConfig = buildLettuceClientConfig(ReadFrom.MASTER);

    RedisStandaloneConfiguration standaloneConfig = new RedisStandaloneConfiguration(
        environment.getProperty("spring.data.redis.host", String.class,
            STANDALONE_REDIS_HOST),
        environment.getProperty("spring.data.redis.port", Integer.class,
            STANDALONE_REDIS_PORT)
    );
    standaloneConfig.setDatabase(database);
    LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(
        standaloneConfig, clientConfig);
    lettuceConnectionFactory.afterPropertiesSet();
    return lettuceConnectionFactory;
  }

  private RedisTemplate<String, String> createRedisTemplate(
      RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, String> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new StringRedisSerializer());
    return template;
  }

  @Bean(name = "credit")
  @Qualifier("creditRedisTemplate")
  public RedisTemplate<String, String> creditRedisTemplate() {
    Integer database = environment.getProperty("config.idempotency.credit", Integer.class);
    return database == null ? null : createRedisTemplate(standaloneFactory(database));
  }

  @Bean
  @Qualifier("installmentRedisTemplate")
  public RedisTemplate<String, String> installmentRedisTemplate() {
    Integer database = environment.getProperty("config.idempotency.installment", Integer.class);
    return database == null ? null : createRedisTemplate(standaloneFactory(database));
  }

  @Bean
  public RedisTemplate<String, String> redisTemplate() {
    Integer database = environment.getProperty("spring.data.redis.database", Integer.class);
    return database == null ? null : createRedisTemplate(standaloneFactory(database));
  }
}
