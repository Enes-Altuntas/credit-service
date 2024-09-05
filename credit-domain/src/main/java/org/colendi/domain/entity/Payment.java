package org.colendi.domain.entity;

import java.time.LocalDate;
import java.util.UUID;
import org.colendi.domain.valueobject.InstallmentId;
import org.colendi.domain.valueobject.PaymentAmount;
import org.colendi.domain.valueobject.PaymentId;

public class Payment extends BaseEntity<PaymentId> {

  private final InstallmentId installmentId;
  private final PaymentAmount paymentAmount;
  private LocalDate createdAt;
  private LocalDate updatedAt;

  public Payment(InstallmentId installmentId, PaymentAmount paymentAmount) {
    super.setId(new PaymentId(UUID.randomUUID()));
    this.installmentId = installmentId;
    this.paymentAmount = paymentAmount;
  }

  public InstallmentId getInstallmentId() {
    return installmentId;
  }

  public LocalDate getCreatedAt() {
    return createdAt;
  }

  public LocalDate getUpdatedAt() {
    return updatedAt;
  }

  public PaymentAmount getPaymentAmount() {
    return paymentAmount;
  }
}
