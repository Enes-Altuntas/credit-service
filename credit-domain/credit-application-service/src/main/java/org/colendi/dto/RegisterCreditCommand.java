package org.colendi.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RegisterCreditCommand {

  @NotBlank(message = "User id cannot be blank")
  private String userId;

  @NotNull
  @Min(value = 100, message = "Credit amount must be greater than 100")
  private double amount;

  @NotNull
  @Max(value = 48, message = "InstallmentResponse count must be less than 48")
  @Min(value = 2, message = "InstallmentResponse count must be greater than 2")
  private Integer installmentCount;

}
