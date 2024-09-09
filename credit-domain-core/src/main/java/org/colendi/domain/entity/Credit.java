package org.colendi.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.colendi.domain.exception.CreditDomainException;
import org.colendi.domain.valueobject.CreditAmount;
import org.colendi.domain.valueobject.CreditId;
import org.colendi.domain.valueobject.CreditStatus;
import org.colendi.domain.valueobject.InstallmentAmount;
import org.colendi.domain.valueobject.InstallmentId;
import org.colendi.domain.valueobject.InstallmentStatus;
import org.colendi.domain.valueobject.UserId;

@Getter
@Setter
public class Credit extends AggregateRoot<CreditId> {

  private final UserId userId;
  private final Integer installmentsCount;
  private CreditAmount creditAmount;
  private CreditStatus status;
  private List<Installment> installments;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public void validateCreditInitialization() {
    validateCreditState();
    validateInstallmentCount();
    validateInstallmentAmount();
    validateInitialInstallments();
  }

  public void validateInstallmentCount() {
    if(Objects.nonNull(this.installmentsCount) && this.installmentsCount <= 0) {
      throw new CreditDomainException("Credit installment cannot be less than or equal to zero");
    }
  }

  public void validateInstallmentAmount() {
    if(Objects.isNull(this.creditAmount.getValue())
        || this.creditAmount.getValue().compareTo(BigDecimal.ZERO) <= 0) {
      throw new CreditDomainException("Credit amount cannot be less than or equal to zero");
    }
  }

  public void validateCreditState() {
    if(Objects.isNull(super.getId()) || Objects.nonNull(this.status)) {
      throw new CreditDomainException("Credit is not in the correct state to be initialized");
    }
  }

  public void validateInitialInstallments() {
    if(Objects.isNull(this.installments) || !this.installments.isEmpty()) {
      throw new CreditDomainException("Credit installments are not in the correct state to be initialized");
    }
  }

  public Installment getInstallmentById(UUID installmentId) {
    return this.installments.stream()
        .filter(installment -> installment.getId().getValue().equals(installmentId))
        .findAny()
        .orElseThrow(() -> new CreditDomainException("Installment not found"));
  }

  public List<Installment> getOverduedInstallments() {
    return this.installments.stream()
        .filter(Installment::isOverdue).toList();
  }

  public BigDecimal getTotalAmountOfInstallments() {
    return this.installments.stream()
        .map(installment -> installment.getInstallmentAmount().getValue())
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public void initializeCredit() {
    this.status = CreditStatus.OPEN;
  }

  public void initializeInstallments() {
    BigDecimal perInstallmentAmount = creditAmount.calculatePerInstallmentAmount(BigDecimal.valueOf(this.installmentsCount));
    LocalDate previousInstallmentDueDate = null;

    for (int i = 0; i < installmentsCount; i++) {
      Installment currentInstallment = Installment.builder()
          .installmentId(new InstallmentId(UUID.randomUUID()))
          .payments(new ArrayList<>())
          .installmentAmount(new InstallmentAmount(perInstallmentAmount))
          .creditId(super.getId())
          .build();

      currentInstallment.validateInstallmentInitialization();

      currentInstallment.initializeInstallment(previousInstallmentDueDate);

      previousInstallmentDueDate = currentInstallment.getDueDate().getValue();

      this.installments.add(currentInstallment);
    }
  }

  public void applyRateOfInterest(BigDecimal rateOfInterest) {
    List<Installment> overduedInstallments = getOverduedInstallments();

    overduedInstallments.forEach(installment -> {
      installment.applyRateOfInterest(rateOfInterest);
      installment.applyStatusChange();
    });

    BigDecimal totalAmountOfInstallments = getTotalAmountOfInstallments();
    this.creditAmount = new CreditAmount(totalAmountOfInstallments);
  }

  public void applyStatusChange() {
    boolean isThereUnpaidInstallment = this.installments.stream()
        .anyMatch(installment -> installment.getStatus() != InstallmentStatus.PAID);

    if(isThereUnpaidInstallment) {
      this.status = CreditStatus.OPEN;
    } else {
      this.status = CreditStatus.CLOSED;
    }
  }

  private Credit(Builder builder) {
    super.setId(builder.creditId);
    userId = builder.userId;
    creditAmount = builder.creditAmount;
    installmentsCount = builder.installmentsCount;
    status = builder.status;
    installments = builder.installments;
    createdAt = builder.createdAt;
    updatedAt = builder.updatedAt;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private CreditId creditId;
    private UserId userId;
    private CreditAmount creditAmount;
    private CreditStatus status;
    private Integer installmentsCount;
    private List<Installment> installments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Builder() {
    }

    public Builder creditId(CreditId val) {
      creditId = val;
      return this;
    }

    public Builder userId(UserId val) {
      userId = val;
      return this;
    }

    public Builder creditAmount(CreditAmount val) {
      creditAmount = val;
      return this;
    }

    public Builder status(CreditStatus val) {
      status = val;
      return this;
    }

    public Builder installmentsCount(Integer val) {
      installmentsCount = val;
      return this;
    }

    public Builder installments(List<Installment> val) {
      installments = val;
      return this;
    }

    public Builder createdAt(LocalDateTime val) {
      createdAt = val;
      return this;
    }

    public Builder updatedAt(LocalDateTime val) {
      updatedAt = val;
      return this;
    }

    public Credit build() {
      return new Credit(this);
    }
  }
}
