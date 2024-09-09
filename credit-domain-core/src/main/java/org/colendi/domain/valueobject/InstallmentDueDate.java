package org.colendi.domain.valueobject;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class InstallmentDueDate {

  private final LocalDate value;

  public InstallmentDueDate(LocalDate value) {
    this.value = value;
  }
  public long getOverdueDays() {
    return ChronoUnit.DAYS.between(this.value, LocalDate.now());
  }
}
