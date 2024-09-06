package org.colendi.domain.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.colendi.domain.exception.CreditDomainException;
import org.colendi.domain.valueobject.CreditId;
import org.colendi.domain.valueobject.InstallmentAmount;
import org.colendi.domain.valueobject.InstallmentDueDate;
import org.colendi.domain.valueobject.InstallmentId;
import org.colendi.domain.valueobject.InstallmentStatus;
import org.colendi.domain.valueobject.PaymentAmount;
import org.colendi.domain.valueobject.PaymentId;

@Getter
@Setter
public class Installment extends BaseEntity<InstallmentId> {
  private final CreditId creditId;
  private InstallmentAmount installmentAmount;
  private InstallmentDueDate dueDate;
  private InstallmentStatus status;
  private List<Payment> payments;

  public void validateInstallmentState() {
    if(Objects.isNull(super.getId()) || Objects.nonNull(this.status)) {
      throw new CreditDomainException("Installment is not in the correct state to be initialized");
    }
  }

  public void validateInstallmentAmount() {
    if(Objects.isNull(installmentAmount) || this.installmentAmount.getValue().compareTo(BigDecimal.ZERO) <= 0) {
      throw new CreditDomainException("Installment amount cannot be less than or equal to zero");
    }
  }

  public void validateInitialPayments() {
    if(Objects.isNull(this.payments) || !this.payments.isEmpty()) {
      throw new CreditDomainException("Installments are not in the correct state to be initialized");
    }
  }

  public void validateInstallmentInitialization() {
    validateInstallmentState();
    validateInstallmentAmount();
    validateInitialPayments();
  }

  public void validatePaymentAmount(BigDecimal paymentAmount) {
    BigDecimal leftPaymentAmount = calculateLeftPaymentAmount();

    if(Objects.isNull(paymentAmount) || paymentAmount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new CreditDomainException("Installment payment amount cannot be less than or equal to zero");
    }
    if(leftPaymentAmount.compareTo(paymentAmount) < 0) {
      throw new CreditDomainException("Installment payment amount cannot be greater than installment left amount, left amount: " + leftPaymentAmount + " TRY");
    }
  }

  public void initializeInstallment(LocalDate previousInstallmentDueDate) {
    this.status = InstallmentStatus.UNPAID;
    LocalDate localDate = calculateDueDate(previousInstallmentDueDate);
    this.dueDate = new InstallmentDueDate(localDate);
  }

  private LocalDate calculateDueDate(LocalDate date) {
    LocalDate dueDate;

    if(Objects.isNull(date)) {
      dueDate = LocalDate.now().plusDays(30);
    } else {
      dueDate = date.plusDays(30);
    }

    DayOfWeek dayOfWeek = dueDate.getDayOfWeek();

    if (dayOfWeek == DayOfWeek.SATURDAY) {
      return dueDate.plusDays(2);
    } else if (dayOfWeek == DayOfWeek.SUNDAY) {
      return dueDate.plusDays(1);
    } else {
      return dueDate;
    }
  }

  public Payment applyPayment(BigDecimal paymentAmount) {
    Payment payment = Payment.builder()
        .paymentId(new PaymentId(UUID.randomUUID()))
        .installmentId(super.getId())
        .paymentAmount(new PaymentAmount(paymentAmount))
        .build();

    payment.validatePaymentInitialization();
    this.payments.add(payment);

    return payment;
}

  public void applyStatusChange() {
    BigDecimal leftPaymentAmount = calculateLeftPaymentAmount();
    if(leftPaymentAmount.compareTo(BigDecimal.ZERO) == 0) {
      this.status = InstallmentStatus.PAID;
    } else if (leftPaymentAmount.compareTo(BigDecimal.ZERO) != 0) {
      if(Boolean.TRUE.equals(this.isOverdue())) {
        this.status = InstallmentStatus.OVERDUE;
      } else {
        if(leftPaymentAmount.compareTo(BigDecimal.ZERO) >= 0) {
          this.status = InstallmentStatus.PARTIALLY_PAID;
        } else {
          this.status = InstallmentStatus.UNPAID;
        }
      }
    }
  }

  public Boolean isOverdue() {
    return this.dueDate.getValue().isBefore(LocalDate.now());
  }

  public BigDecimal calculateLeftPaymentAmount() {
    if(Objects.isNull(this.payments) || this.payments.isEmpty()) {
      return this.installmentAmount.getValue();
    }

    BigDecimal totalPaidPayments = this.payments.stream()
        .map(payment -> payment.getPaymentAmount().getValue()) // Accessing the BigDecimal in Amount
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    return this.installmentAmount.getValue().subtract(totalPaidPayments);
  }

  public void applyRateOfInterest(BigDecimal rateOfInterest) {
    long daysBetween = this.dueDate.getOverdueDays();

    BigDecimal interestAmount = calculateInterestAmount(rateOfInterest, daysBetween);

    this.installmentAmount = new InstallmentAmount(this.installmentAmount.getValue().add(interestAmount));
  }

  public BigDecimal calculateInterestAmount(BigDecimal rateOfInterest, long daysBetween) {
    return (BigDecimal.valueOf(daysBetween)
        .multiply(rateOfInterest.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_EVEN))
        .multiply(this.installmentAmount.getValue()))
        .divide(BigDecimal.valueOf(365), 2, RoundingMode.HALF_EVEN);
  }

  private Installment(Builder builder) {
    super.setId(builder.installmentId);
    creditId = builder.creditId;
    installmentAmount = builder.installmentAmount;
    dueDate = builder.dueDate;
    status = builder.status;
    payments = builder.payments;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private InstallmentId installmentId;
    private CreditId creditId;
    private InstallmentAmount installmentAmount;
    private InstallmentDueDate dueDate;
    private InstallmentStatus status;
    private List<Payment> payments;

    private Builder() {
    }

    public Builder installmentId(InstallmentId val) {
      installmentId = val;
      return this;
    }

    public Builder creditId(CreditId val) {
      creditId = val;
      return this;
    }

    public Builder installmentAmount(InstallmentAmount val) {
      installmentAmount = val;
      return this;
    }

    public Builder dueDate(InstallmentDueDate val) {
      dueDate = val;
      return this;
    }

    public Builder status(InstallmentStatus val) {
      status = val;
      return this;
    }

    public Builder payments(List<Payment> val) {
      payments = val;
      return this;
    }

    public Installment build() {
      return new Installment(this);
    }
  }
}
