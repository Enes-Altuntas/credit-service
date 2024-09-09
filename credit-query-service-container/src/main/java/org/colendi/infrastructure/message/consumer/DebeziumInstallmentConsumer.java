package org.colendi.infrastructure.message.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.colendi.infrastructure.message.consumer.model.CreditMessage;
import org.colendi.infrastructure.message.consumer.model.InstallmentMessage;
import org.colendi.usecase.ports.output.nosql.CreditMongoRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DebeziumInstallmentConsumer {

  private final ObjectMapper objectMapper = new ObjectMapper();

  private final CreditMongoRepository creditMongoRepository;

  private final RedisTemplate<String, String> installmentRedisTemplate;

  public DebeziumInstallmentConsumer(CreditMongoRepository creditMongoRepository,
      @Qualifier("installmentRedisTemplate") RedisTemplate<String, String> installmentRedisTemplate) {
    this.creditMongoRepository = creditMongoRepository;
    this.installmentRedisTemplate = installmentRedisTemplate;
  }

  @KafkaListener(topics = "postgres.public.installments", groupId = "credit-query-service")
  public void installmentConsumer(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
    log.info("Received message: {}", record.value());
    try {
      String installmentId = objectMapper.readTree(record.key()).get("id").asText();

      JsonNode messageNode = objectMapper.readTree(record.value());

      JsonNode afterNode = messageNode.path("after");
      InstallmentMessage messageModel = objectMapper.treeToValue(afterNode, InstallmentMessage.class);
      if(Objects.isNull(messageModel)) {
        return;
      }

      String messageFromRedis = getMessageFromRedis(installmentId);
      if(messageFromRedis != null && messageFromRedis.equals(record.value())) {
        log.info("InstallmentId: {} already processed", installmentId);
        acknowledgment.acknowledge();
        return;
      }

      putMessageToRedis(installmentId, record.value());

      creditMongoRepository.saveInstallment(messageModel);
      log.info("Message: {} saved to mongo", messageModel);
      acknowledgment.acknowledge();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  public void putMessageToRedis(String key, String value) {
    installmentRedisTemplate.opsForValue().set(key, value, 30, TimeUnit.MINUTES);
  }

  public String getMessageFromRedis(String key) {
    return installmentRedisTemplate.opsForValue().get(key);
  }
}
