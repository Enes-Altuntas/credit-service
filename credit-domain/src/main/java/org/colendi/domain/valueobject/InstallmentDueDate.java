package org.colendi.domain.valueobject;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Objects;

public class InstallmentDueDate {

  private final LocalDate value;

  public InstallmentDueDate(LocalDate previousInstallmentDueDate) {
    if(Objects.isNull(previousInstallmentDueDate)) {
      this.value = calculateDueDate(LocalDate.now());
    } else {
      this.value = calculateDueDate(previousInstallmentDueDate.plusDays(30));
    }
  }
  public LocalDate getValue() {
    return this.value;
  }

  private LocalDate calculateDueDate(LocalDate date) {
    DayOfWeek dayOfWeek = date.getDayOfWeek();

    if (dayOfWeek == DayOfWeek.SATURDAY) {
      return date.plusDays(2);
    } else if (dayOfWeek == DayOfWeek.SUNDAY) {
      return date.plusDays(1);
    } else {
      return date;
    }
  }
}
