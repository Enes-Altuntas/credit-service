package org.colendi.domain.entity;

import java.math.BigDecimal;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.colendi.domain.exception.CreditDomainException;
import org.colendi.domain.valueobject.InstallmentId;
import org.colendi.domain.valueobject.PaymentAmount;
import org.colendi.domain.valueobject.PaymentId;

@Getter
@Setter
public class Payment extends BaseEntity<PaymentId> {

  private final InstallmentId installmentId;
  private final PaymentAmount paymentAmount;

  public void validatePaymentState() {
    if(Objects.isNull(super.getId())) {
      throw new CreditDomainException("Payment is not in the correct state to be initialized");
    }
  }

  public void validatePaymentAmount() {
    if(Objects.isNull(paymentAmount) || this.paymentAmount.getValue().compareTo(BigDecimal.ZERO) <= 0) {
      throw new CreditDomainException("Payment amount cannot be less than or equal to zero");
    }
  }

  public void validatePaymentInitialization() {
    validatePaymentState();
    validatePaymentAmount();
  }

  private Payment(Builder builder) {
    super.setId(builder.paymentId);
    installmentId = builder.installmentId;
    paymentAmount = builder.paymentAmount;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private PaymentId paymentId;
    private PaymentAmount paymentAmount;
    private InstallmentId installmentId;

    private Builder() {
    }

    public Builder paymentId(PaymentId val) {
      paymentId = val;
      return this;
    }

    public Builder paymentAmount(PaymentAmount val) {
      paymentAmount = val;
      return this;
    }

    public Builder installmentId(InstallmentId val) {
      installmentId = val;
      return this;
    }

    public Payment build() {
      return new Payment(this);
    }
  }
}
