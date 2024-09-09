package org.colendi.infrastructure.database.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.colendi.domain.valueobject.InstallmentStatus;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "installments", indexes = {
  @Index(name = "idx_credit_id", columnList = "credit_id")
})
@Entity
public class InstallmentEntity extends BaseDataEntity {

  @Id
  private UUID id;

  private String amount;

  @Enumerated(EnumType.STRING)
  private InstallmentStatus status;

  private LocalDateTime dueDate;

  private UUID creditId;
}
