package org.colendi.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.colendi.domain.valueobject.CreditAmount;
import org.colendi.domain.valueobject.CreditId;
import org.colendi.domain.valueobject.CreditStatus;
import org.colendi.domain.valueobject.UserId;

public class Credit extends AggregateRoot<CreditId> {

  private final UserId userId;
  private final CreditAmount creditAmount;
  private final Integer installmentsCount;
  private CreditStatus status;
  private LocalDate createdAt;
  private LocalDate updatedAt;
  private List<Installment> installments;

  public Credit(UserId userId, CreditAmount creditAmount, Integer installmentsCount) {
    this.userId = userId;
    this.creditAmount = creditAmount;
    this.installmentsCount = installmentsCount;
    this.installments = new ArrayList<>();
  }

  public void initializeCredit() {
    setId(new CreditId(UUID.randomUUID()));
    this.status = CreditStatus.OPEN;

    initializeInstallments();
  }

  public void initializeInstallments() {
    BigDecimal perInstallmentAmount = creditAmount.calculatePerInstallmentAmount(BigDecimal.valueOf(this.installmentsCount));
    LocalDate previousInstallmentDueDate = null;

    for (int i = 0; i < installmentsCount; i++) {
      Installment currentInstallment = new Installment(super.getId(), perInstallmentAmount);
      currentInstallment.initializeInstallment(previousInstallmentDueDate);

      previousInstallmentDueDate = currentInstallment.getDueDate().getValue();

      this.installments.add(currentInstallment);
    }
  }

  public UserId getUserId() {
    return this.userId;
  }

  public CreditAmount getCreditAmount() {
    return this.creditAmount;
  }

  public Integer getInstallmentsCount() {
    return this.installmentsCount;
  }

  public CreditStatus getStatus() {
    return this.status;
  }

  public LocalDate getCreatedAt() {
    return createdAt;
  }

  public LocalDate getUpdatedAt() {
    return updatedAt;
  }

  public List<Installment> getInstallments() {
    return this.installments;
  }
}
