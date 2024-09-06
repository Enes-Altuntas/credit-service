package org.colendi.mapper;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;
import org.colendi.domain.entity.Credit;
import org.colendi.domain.entity.Installment;
import org.colendi.domain.entity.User;
import org.colendi.domain.valueobject.CreditAmount;
import org.colendi.domain.valueobject.CreditId;
import org.colendi.domain.valueobject.UserId;
import org.colendi.dto.InstallmentResponse;
import org.colendi.dto.RegisterCreditCommand;
import org.colendi.dto.RegisterCreditResponse;
import org.springframework.stereotype.Component;

@Component
public class CreditDomainMapper {

  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  public Credit registerCreditCommandMapper(RegisterCreditCommand command) {
    return Credit.builder()
        .userId(new UserId(UUID.fromString(command.getUserId())))
        .creditAmount(new CreditAmount(BigDecimal.valueOf(command.getAmount())))
        .creditId(new CreditId(UUID.randomUUID()))
        .installmentsCount(command.getInstallmentCount())
        .installments(new ArrayList<>())
        .build();
  }

  public RegisterCreditResponse registerCreditResponseMapper(Credit credit) {
    return RegisterCreditResponse.builder()
        .creditId(credit.getId().getValue().toString())
        .installmentResponses(credit.getInstallments().stream().map(this::installmentResponseMapper).toList())
        .build();
  }

  private InstallmentResponse installmentResponseMapper(Installment installment) {
    return InstallmentResponse.builder()
        .id(installment.getId().getValue().toString())
        .dueDate(installment.getDueDate().getValue().format(formatter))
        .amount(installment.getInstallmentAmount().getValue().doubleValue())
        .build();
  }
}
