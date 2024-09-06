package org.colendi.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
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

  private LocalDate dueDate;

  private UUID creditId;
}
