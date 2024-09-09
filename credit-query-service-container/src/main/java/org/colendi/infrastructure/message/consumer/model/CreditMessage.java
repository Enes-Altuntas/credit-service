package org.colendi.infrastructure.message.consumer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;
import lombok.Data;

@Data
public class CreditMessage {

  @JsonProperty("credit_id")
  private String creditId;

  private String status;

  @JsonProperty("installments_count")
  private Integer installmentsCount;

  private String amount;

  @JsonProperty("user_id")
  private String userId;

  @JsonProperty("created_at")
  private Long createdAt;

  @JsonProperty("updated_at")
  private Long updatedAt;
}
