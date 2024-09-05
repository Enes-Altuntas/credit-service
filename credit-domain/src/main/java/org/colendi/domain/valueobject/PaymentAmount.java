package org.colendi.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class PaymentAmount {

  private final BigDecimal value;


  public PaymentAmount(BigDecimal value) {
    this.value = setScale(value);
  }

  public BigDecimal getValue() {
    return this.value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PaymentAmount that = (PaymentAmount) o;
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  private BigDecimal setScale(BigDecimal value) {
    return value.setScale(2, RoundingMode.HALF_EVEN);
  }
}
