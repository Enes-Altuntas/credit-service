package org.colendi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PayInstallmentCommand {

  @NotBlank(message = "Installment id cannot be blank")
  private String creditId;

  @NotBlank(message = "Installment id cannot be blank")
  private String installmentId;

  @NotNull(message = "Payment amount cannot be blank")
  private double amount;

}
