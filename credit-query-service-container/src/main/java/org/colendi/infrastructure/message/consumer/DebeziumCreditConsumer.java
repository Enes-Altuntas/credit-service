package org.colendi.infrastructure.message.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.colendi.infrastructure.message.consumer.model.CreditMessage;
import org.colendi.usecase.ports.output.nosql.CreditMongoRepository;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DebeziumCreditConsumer {

  private final ObjectMapper objectMapper = new ObjectMapper();

  private final CreditMongoRepository creditMongoRepository;

  private final RedisTemplate<String, String> creditRedisTemplate;

  private final RedissonClient redissonClient;

  public DebeziumCreditConsumer(CreditMongoRepository creditMongoRepository,
      @Qualifier("creditRedisTemplate") RedisTemplate<String, String> creditRedisTemplate,
      RedissonClient redissonClient) {
    this.creditMongoRepository = creditMongoRepository;
    this.creditRedisTemplate = creditRedisTemplate;
    this.redissonClient = redissonClient;
  }

  @KafkaListener(topics = "postgres.public.credits", groupId = "credit-query-service")
  public void creditConsumer(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
    log.info("Received message: {}", record.value());
    try {
      String creditId = objectMapper.readTree(record.key()).get("credit_id").asText();

      JsonNode messageNode = objectMapper.readTree(record.value());

      JsonNode afterNode = messageNode.path("after");
      CreditMessage messageModel = objectMapper.treeToValue(afterNode, CreditMessage.class);
      if(Objects.isNull(messageModel)) {
        return;
      }

      String messageFromRedis = getMessageFromRedis(creditId);
      if(messageFromRedis != null && messageFromRedis.equals(record.value())) {
        log.info("CreditId: {} already processed", creditId);
        acknowledgment.acknowledge();
        return;
      }

      putMessageToRedis(creditId, record.value());

      creditMongoRepository.saveCredit(messageModel);
      log.info("Message: {} saved to mongo", messageModel);
      acknowledgment.acknowledge();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  public void putMessageToRedis(String key, String value) {
    creditRedisTemplate.opsForValue().set(key, value, 30, TimeUnit.MINUTES);
  }

  public String getMessageFromRedis(String key) {
    return creditRedisTemplate.opsForValue().get(key);
  }
}
