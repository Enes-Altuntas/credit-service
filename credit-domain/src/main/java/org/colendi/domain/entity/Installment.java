package org.colendi.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.colendi.domain.valueobject.CreditId;
import org.colendi.domain.valueobject.InstallmentAmount;
import org.colendi.domain.valueobject.InstallmentDueDate;
import org.colendi.domain.valueobject.InstallmentId;
import org.colendi.domain.valueobject.InstallmentStatus;

public class Installment extends BaseEntity<InstallmentId> {
  private final CreditId creditId;
  private final InstallmentAmount installmentAmount;
  private InstallmentDueDate dueDate;
  private InstallmentStatus status;
  private LocalDate createdAt;
  private LocalDate updatedAt;
  private List<Payment> payments;

  public Installment(CreditId creditId, BigDecimal installmentAmount) {
    super.setId(new InstallmentId(UUID.randomUUID()));
    this.creditId = creditId;
    this.installmentAmount = new InstallmentAmount(installmentAmount);
    this.payments = new ArrayList<>();
  }

  public void initializeInstallment(LocalDate previousInstallmentDueDate) {
    this.status = InstallmentStatus.UNPAID;
    this.dueDate = new InstallmentDueDate(previousInstallmentDueDate);
  }

  public CreditId getCreditId() {
    return this.creditId;
  }

  public InstallmentAmount getInstallmentAmount() {
    return this.installmentAmount;
  }

  public InstallmentDueDate getDueDate() {
    return this.dueDate;
  }

  public InstallmentStatus getStatus() {
    return this.status;
  }

  public LocalDate getCreatedAt() {
    return createdAt;
  }

  public LocalDate getUpdatedAt() {
    return updatedAt;
  }

  public List<Payment> getPayments() {
    return this.payments;
  }
}
