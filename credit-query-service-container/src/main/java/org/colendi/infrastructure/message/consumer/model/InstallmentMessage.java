package org.colendi.infrastructure.message.consumer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;
import lombok.Data;

@Data
public class InstallmentMessage {
  private String id;

  private String amount;

  private String status;

  @JsonProperty("due_date")
  private Long dueDate;

  @JsonProperty("credit_id")
  private String creditId;

  @JsonProperty("created_at")
  private Long createdAt;

  @JsonProperty("updated_at")
  private Long updatedAt;
}