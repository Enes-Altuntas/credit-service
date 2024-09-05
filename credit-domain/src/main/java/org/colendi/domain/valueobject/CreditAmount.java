package org.colendi.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class CreditAmount {

  private final BigDecimal value;


  public CreditAmount(BigDecimal value) {
    this.value = setScale(value);
  }

  public BigDecimal getValue() {
    return this.value;
  }

  public BigDecimal calculatePerInstallmentAmount(BigDecimal installmentCount) {
    return this.value.divide(installmentCount, 2, RoundingMode.HALF_EVEN);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreditAmount that = (CreditAmount) o;
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
